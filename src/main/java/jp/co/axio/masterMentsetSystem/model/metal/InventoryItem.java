package jp.co.axio.masterMentsetSystem.model.metal;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Current inventory posture for a product.
 */
public class InventoryItem {

    private String productId;
    private BigDecimal quantity;
    private BigDecimal averageCost;

    public InventoryItem() {
    }

    public InventoryItem(String productId, BigDecimal quantity, BigDecimal averageCost) {
        this.productId = productId;
        this.quantity = quantity;
        this.averageCost = averageCost;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryItem)) {
            return false;
        }
        InventoryItem that = (InventoryItem) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
