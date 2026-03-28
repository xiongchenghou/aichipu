package jp.co.axio.masterMentsetSystem.controller.metal;

import jp.co.axio.masterMentsetSystem.dto.metal.CustomerRequest;
import jp.co.axio.masterMentsetSystem.dto.metal.FinancialTransactionRequest;
import jp.co.axio.masterMentsetSystem.dto.metal.ProductRequest;
import jp.co.axio.masterMentsetSystem.dto.metal.ReceiptRequest;
import jp.co.axio.masterMentsetSystem.dto.metal.ShipmentRequest;
import jp.co.axio.masterMentsetSystem.model.metal.Customer;
import jp.co.axio.masterMentsetSystem.model.metal.FinancialSummary;
import jp.co.axio.masterMentsetSystem.model.metal.FinancialTransaction;
import jp.co.axio.masterMentsetSystem.model.metal.InventorySnapshot;
import jp.co.axio.masterMentsetSystem.model.metal.InventoryTransaction;
import jp.co.axio.masterMentsetSystem.model.metal.Product;
import jp.co.axio.masterMentsetSystem.service.metal.MetalRecycleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * REST API for the Japanese metal recycling operations.
 */
@RestController
@RequestMapping("/api/metal-recycle")
@Validated
public class MetalRecycleController {

    private final MetalRecycleService metalRecycleService;

    public MetalRecycleController(MetalRecycleService metalRecycleService) {
        this.metalRecycleService = metalRecycleService;
    }

    @GetMapping("/customers")
    public List<Customer> listCustomers() {
        return metalRecycleService.listCustomers();
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metalRecycleService.createCustomer(request));
    }

    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable String id, @Valid @RequestBody CustomerRequest request) {
        return metalRecycleService.updateCustomer(id, request);
    }

    @GetMapping("/products")
    public List<Product> listProducts() {
        return metalRecycleService.listProducts();
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metalRecycleService.createProduct(request));
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        return metalRecycleService.updateProduct(id, request);
    }

    @GetMapping("/inventory")
    public InventorySnapshot inventorySnapshot() {
        return metalRecycleService.inventorySnapshot();
    }

    @PostMapping("/receipts")
    public ResponseEntity<InventoryTransaction> recordReceipt(@Valid @RequestBody ReceiptRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metalRecycleService.recordReceipt(request));
    }

    @PostMapping("/shipments")
    public ResponseEntity<InventoryTransaction> recordShipment(@Valid @RequestBody ShipmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metalRecycleService.recordShipment(request));
    }

    @GetMapping("/transactions")
    public List<InventoryTransaction> transactionHistory() {
        return metalRecycleService.transactionHistory();
    }

    @PostMapping("/finance/transactions")
    public ResponseEntity<FinancialTransaction> recordFinancial(@Valid @RequestBody FinancialTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(metalRecycleService.recordFinancialTransaction(request));
    }

    @GetMapping("/finance/transactions")
    public List<FinancialTransaction> financialTransactions() {
        return metalRecycleService.financialTransactions();
    }

    @GetMapping("/finance/summary")
    public FinancialSummary financialSummary() {
        return metalRecycleService.financialSummary();
    }
}
