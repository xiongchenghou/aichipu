package jp.co.axio.masterMentsetSystem.model.metal;

import java.util.List;

/**
 * Snapshot combining inventory items with enriched product and customer data for presentation layers.
 */
public class InventorySnapshot {

    private List<InventoryItem> items;

    public InventorySnapshot() {
    }

    public InventorySnapshot(List<InventoryItem> items) {
        this.items = items;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }
}
