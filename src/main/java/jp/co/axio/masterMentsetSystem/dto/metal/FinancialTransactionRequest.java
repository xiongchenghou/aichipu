package jp.co.axio.masterMentsetSystem.dto.metal;

import jp.co.axio.masterMentsetSystem.model.metal.FinancialTransaction;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Request payload for manual financial entries and adjustments.
 */
public class FinancialTransactionRequest {

    @NotNull
    private FinancialTransaction.Type type;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    @Size(max = 3)
    private String currency = "JPY";

    @Size(max = 255)
    private String referenceNumber;

    @Size(max = 255)
    private String relatedCustomerId;

    @Size(max = 1000)
    private String description;

    public FinancialTransaction.Type getType() {
        return type;
    }

    public void setType(FinancialTransaction.Type type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getRelatedCustomerId() {
        return relatedCustomerId;
    }

    public void setRelatedCustomerId(String relatedCustomerId) {
        this.relatedCustomerId = relatedCustomerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
