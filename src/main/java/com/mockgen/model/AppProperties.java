package com.mockgen.model;

public class AppProperties {
    private String csvInputPath;
    private String filterProductId;
    private int topSalesCount;
    private char csvSeparator;
    private String reportOutput;

    // Getters and Setters
    public String getCsvInputPath() { return csvInputPath; }
    public void setCsvInputPath(String csvInputPath) { this.csvInputPath = csvInputPath; }

    public String getFilterProductId() { return filterProductId; }
    public void setFilterProductId(String filterProductId) { this.filterProductId = filterProductId; }

    public int getTopSalesCount() { return topSalesCount; }
    public void setTopSalesCount(int topSalesCount) { this.topSalesCount = topSalesCount; }

    public char getCsvSeparator() { return csvSeparator; }
    public void setCsvSeparator(char csvSeparator) { this.csvSeparator = csvSeparator; }

    public String getReportOutput() { return reportOutput; }
    public void setReportOutput(String reportOutput) { this.reportOutput = reportOutput; }
}