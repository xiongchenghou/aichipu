package jp.co.axio.masterMentsetSystem.dto.metal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Request payload for product creation and updates.
 */
public class ProductRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String kanaName;

    @Size(max = 255)
    private String category;

    @Size(max = 20)
    private String unit;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal standardCost;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal sellingPrice;

    private boolean hazardous;

    @Size(max = 1000)
    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKanaName() {
        return kanaName;
    }

    public void setKanaName(String kanaName) {
        this.kanaName = kanaName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getStandardCost() {
        return standardCost;
    }

    public void setStandardCost(BigDecimal standardCost) {
        this.standardCost = standardCost;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public boolean isHazardous() {
        return hazardous;
    }

    public void setHazardous(boolean hazardous) {
        this.hazardous = hazardous;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
