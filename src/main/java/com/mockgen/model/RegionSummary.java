package com.mockgen.model;

import java.math.BigDecimal;

public class RegionSummary {
    private String region;
    private BigDecimal totalAmount;

    public RegionSummary(String region, BigDecimal totalAmount) {
        this.region = region;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getRegion() { return region; }
    public BigDecimal getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return String.format("%s: %.2f", region, totalAmount);
    }
}