package com.mockgen.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private String transactionId;
    private LocalDate date;
    private String productId;
    private BigDecimal amount;
    private String region;

    public Transaction() {}

    public Transaction(String transactionId, LocalDate date, String productId,
                       BigDecimal amount, String region) {
        this.transactionId = transactionId;
        this.date = date;
        this.productId = productId;
        this.amount = amount;
        this.region = region;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%.2f,%s",
                transactionId, date, productId, amount, region);
    }
}