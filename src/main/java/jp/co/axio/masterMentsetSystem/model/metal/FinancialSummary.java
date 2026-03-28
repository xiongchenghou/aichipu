package jp.co.axio.masterMentsetSystem.model.metal;

import java.math.BigDecimal;

/**
 * Aggregated financial indicators for dashboards.
 */
public class FinancialSummary {

    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal netIncome;

    public FinancialSummary() {
    }

    public FinancialSummary(BigDecimal totalRevenue, BigDecimal totalExpense, BigDecimal netIncome) {
        this.totalRevenue = totalRevenue;
        this.totalExpense = totalExpense;
        this.netIncome = netIncome;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(BigDecimal netIncome) {
        this.netIncome = netIncome;
    }
}
