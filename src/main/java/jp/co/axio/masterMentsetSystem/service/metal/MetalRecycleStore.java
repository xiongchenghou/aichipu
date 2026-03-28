package jp.co.axio.masterMentsetSystem.service.metal;

import jp.co.axio.masterMentsetSystem.model.metal.Customer;
import jp.co.axio.masterMentsetSystem.model.metal.FinancialTransaction;
import jp.co.axio.masterMentsetSystem.model.metal.InventoryItem;
import jp.co.axio.masterMentsetSystem.model.metal.InventoryTransaction;
import jp.co.axio.masterMentsetSystem.model.metal.Product;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Simple in-memory persistence used for prototyping the recycling workflows.
 */
@Repository
public class MetalRecycleStore {

    private final Map<String, Customer> customers = new ConcurrentHashMap<>();
    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private final Map<String, InventoryItem> inventory = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<InventoryTransaction> transactions = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<FinancialTransaction> financialTransactions = new CopyOnWriteArrayList<>();

    public Collection<Customer> findAllCustomers() {
        return customers.values();
    }

    public Optional<Customer> findCustomer(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    public Customer saveCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Collection<Product> findAllProducts() {
        return products.values();
    }

    public Optional<Product> findProduct(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public Product saveProduct(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    public InventoryItem saveInventoryItem(InventoryItem item) {
        inventory.put(item.getProductId(), item);
        return item;
    }

    public Optional<InventoryItem> findInventoryItem(String productId) {
        return Optional.ofNullable(inventory.get(productId));
    }

    public List<InventoryItem> findAllInventoryItems() {
        return inventory.values().stream()
                .map(item -> new InventoryItem(item.getProductId(), item.getQuantity(), item.getAverageCost()))
                .collect(Collectors.toList());
    }

    public InventoryTransaction saveTransaction(InventoryTransaction transaction) {
        transactions.add(transaction);
        return transaction;
    }

    public List<InventoryTransaction> findAllTransactions() {
        return List.copyOf(transactions);
    }

    public FinancialTransaction saveFinancialTransaction(FinancialTransaction transaction) {
        financialTransactions.add(transaction);
        return transaction;
    }

    public List<FinancialTransaction> findAllFinancialTransactions() {
        return List.copyOf(financialTransactions);
    }
}
