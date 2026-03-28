package jp.co.axio.masterMentsetSystem.service.metal;

import jp.co.axio.masterMentsetSystem.dto.metal.CustomerRequest;
import jp.co.axio.masterMentsetSystem.dto.metal.FinancialTransactionRequest;
import jp.co.axio.masterMentsetSystem.dto.metal.ProductRequest;
import jp.co.axio.masterMentsetSystem.dto.metal.ReceiptRequest;
import jp.co.axio.masterMentsetSystem.dto.metal.ShipmentRequest;
import jp.co.axio.masterMentsetSystem.model.metal.Customer;
import jp.co.axio.masterMentsetSystem.model.metal.FinancialSummary;
import jp.co.axio.masterMentsetSystem.model.metal.FinancialTransaction;
import jp.co.axio.masterMentsetSystem.model.metal.InventoryItem;
import jp.co.axio.masterMentsetSystem.model.metal.InventorySnapshot;
import jp.co.axio.masterMentsetSystem.model.metal.InventoryTransaction;
import jp.co.axio.masterMentsetSystem.model.metal.Product;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Core business service orchestrating the lifecycle of material, partners and finance entries.
 */
@Service
public class MetalRecycleService {

    private static final MathContext MONEY_MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);

    private final MetalRecycleStore store;

    public MetalRecycleService(MetalRecycleStore store) {
        this.store = store;
    }

    public List<Customer> listCustomers() {
        return store.findAllCustomers().stream()
                .sorted(Comparator.comparing(Customer::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public Customer createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setCreatedAt(OffsetDateTime.now());
        applyCustomerRequest(customer, request);
        return store.saveCustomer(customer);
    }

    public Customer updateCustomer(String id, CustomerRequest request) {
        Customer existing = store.findCustomer(id)
                .orElseThrow(() -> new ValidationException("Customer not found: " + id));
        applyCustomerRequest(existing, request);
        existing.setUpdatedAt(OffsetDateTime.now());
        return store.saveCustomer(existing);
    }

    private void applyCustomerRequest(Customer target, CustomerRequest request) {
        target.setName(request.getName());
        target.setKanaName(request.getKanaName());
        target.setContactPerson(request.getContactPerson());
        target.setPhone(request.getPhone());
        target.setEmail(request.getEmail());
        target.setAddress(request.getAddress());
        target.setTaxId(request.getTaxId());
        target.setNotes(request.getNotes());
    }

    public List<Product> listProducts() {
        return store.findAllProducts().stream()
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setCreatedAt(OffsetDateTime.now());
        applyProductRequest(product, request);
        return store.saveProduct(product);
    }

    public Product updateProduct(String id, ProductRequest request) {
        Product existing = store.findProduct(id)
                .orElseThrow(() -> new ValidationException("Product not found: " + id));
        applyProductRequest(existing, request);
        existing.setUpdatedAt(OffsetDateTime.now());
        return store.saveProduct(existing);
    }

    private void applyProductRequest(Product target, ProductRequest request) {
        target.setName(request.getName());
        target.setKanaName(request.getKanaName());
        target.setCategory(request.getCategory());
        target.setUnit(request.getUnit());
        target.setStandardCost(request.getStandardCost());
        target.setSellingPrice(request.getSellingPrice());
        target.setHazardous(request.isHazardous());
        target.setNotes(request.getNotes());
    }

    public InventorySnapshot inventorySnapshot() {
        return new InventorySnapshot(store.findAllInventoryItems());
    }

    public InventoryTransaction recordReceipt(ReceiptRequest request) {
        Product product = store.findProduct(request.getProductId())
                .orElseThrow(() -> new ValidationException("Product not found: " + request.getProductId()));

        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Quantity must be positive");
        }

        if (request.getUnitCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Unit cost must be positive");
        }

        InventoryItem current = store.findInventoryItem(product.getId())
                .orElseGet(() -> new InventoryItem(product.getId(), BigDecimal.ZERO, BigDecimal.ZERO));

        BigDecimal existingQuantity = current.getQuantity();
        BigDecimal existingCost = current.getAverageCost();
        BigDecimal newQuantity = existingQuantity.add(request.getQuantity());
        BigDecimal weightedCost = existingCost.multiply(existingQuantity, MONEY_MATH_CONTEXT)
                .add(request.getUnitCost().multiply(request.getQuantity(), MONEY_MATH_CONTEXT), MONEY_MATH_CONTEXT);
        BigDecimal newAverageCost = newQuantity.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : weightedCost.divide(newQuantity, MONEY_MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);

        current.setQuantity(newQuantity.setScale(3, RoundingMode.HALF_UP));
        current.setAverageCost(newAverageCost);
        store.saveInventoryItem(current);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setType(InventoryTransaction.Type.RECEIPT);
        transaction.setProductId(product.getId());
        transaction.setQuantity(request.getQuantity().setScale(3, RoundingMode.HALF_UP));
        transaction.setUnitCost(request.getUnitCost().setScale(2, RoundingMode.HALF_UP));
        transaction.setReferenceNumber(request.getReferenceNumber());
        transaction.setNotes(request.getNotes());
        transaction.setOccurredAt(OffsetDateTime.now());
        store.saveTransaction(transaction);

        if (StringUtils.hasText(request.getSupplierName())) {
            FinancialTransaction supplierPayment = new FinancialTransaction();
            supplierPayment.setId(UUID.randomUUID().toString());
            supplierPayment.setType(FinancialTransaction.Type.EXPENSE);
            supplierPayment.setAmount(request.getUnitCost().multiply(request.getQuantity(), MONEY_MATH_CONTEXT)
                    .setScale(2, RoundingMode.HALF_UP));
            supplierPayment.setCurrency("JPY");
            supplierPayment.setReferenceNumber(request.getReferenceNumber());
            supplierPayment.setDescription("仕入先:" + request.getSupplierName());
            supplierPayment.setOccurredAt(transaction.getOccurredAt());
            store.saveFinancialTransaction(supplierPayment);
        }

        return transaction;
    }

    public InventoryTransaction recordShipment(ShipmentRequest request) {
        Product product = store.findProduct(request.getProductId())
                .orElseThrow(() -> new ValidationException("Product not found: " + request.getProductId()));

        InventoryItem current = store.findInventoryItem(product.getId())
                .orElseGet(() -> new InventoryItem(product.getId(), BigDecimal.ZERO, BigDecimal.ZERO));

        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Quantity must be positive");
        }

        if (current.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new ValidationException("Insufficient inventory for shipment");
        }

        current.setQuantity(current.getQuantity().subtract(request.getQuantity()).setScale(3, RoundingMode.HALF_UP));
        store.saveInventoryItem(current);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setType(InventoryTransaction.Type.SHIPMENT);
        transaction.setProductId(product.getId());
        transaction.setQuantity(request.getQuantity().setScale(3, RoundingMode.HALF_UP));
        transaction.setUnitCost(current.getAverageCost());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setReferenceNumber(request.getReferenceNumber());
        transaction.setNotes(request.getNotes());
        transaction.setOccurredAt(OffsetDateTime.now());
        store.saveTransaction(transaction);

        if (request.getUnitPrice() != null && request.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            FinancialTransaction revenue = new FinancialTransaction();
            revenue.setId(UUID.randomUUID().toString());
            revenue.setType(FinancialTransaction.Type.REVENUE);
            revenue.setAmount(request.getUnitPrice().multiply(request.getQuantity(), MONEY_MATH_CONTEXT)
                    .setScale(2, RoundingMode.HALF_UP));
            revenue.setCurrency("JPY");
            revenue.setReferenceNumber(request.getReferenceNumber());
            revenue.setRelatedCustomerId(request.getCustomerId());
            revenue.setDescription("出荷売上:" + product.getName());
            revenue.setOccurredAt(transaction.getOccurredAt());
            store.saveFinancialTransaction(revenue);
        }

        return transaction;
    }

    public List<InventoryTransaction> transactionHistory() {
        return store.findAllTransactions().stream()
                .sorted(Comparator.comparing(InventoryTransaction::getOccurredAt).reversed())
                .collect(Collectors.toList());
    }

    public FinancialTransaction recordFinancialTransaction(FinancialTransactionRequest request) {
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount().setScale(2, RoundingMode.HALF_UP));
        transaction.setCurrency(StringUtils.hasText(request.getCurrency()) ? request.getCurrency() : "JPY");
        transaction.setReferenceNumber(request.getReferenceNumber());
        transaction.setRelatedCustomerId(request.getRelatedCustomerId());
        transaction.setDescription(request.getDescription());
        transaction.setOccurredAt(OffsetDateTime.now());
        return store.saveFinancialTransaction(transaction);
    }

    public List<FinancialTransaction> financialTransactions() {
        return store.findAllFinancialTransactions().stream()
                .sorted(Comparator.comparing(FinancialTransaction::getOccurredAt).reversed())
                .collect(Collectors.toList());
    }

    public FinancialSummary financialSummary() {
        BigDecimal revenue = financialTransactions().stream()
                .filter(tx -> tx.getType() == FinancialTransaction.Type.REVENUE)
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = financialTransactions().stream()
                .filter(tx -> tx.getType() == FinancialTransaction.Type.EXPENSE)
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FinancialSummary(revenue.setScale(2, RoundingMode.HALF_UP),
                expense.setScale(2, RoundingMode.HALF_UP),
                revenue.subtract(expense, MONEY_MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP));
    }
}
