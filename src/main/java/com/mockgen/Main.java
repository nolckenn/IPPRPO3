package com.mockgen;

import com.mockgen.config.AppConfig;
import com.mockgen.service.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize configuration
            AppConfig appConfig = new AppConfig();

            // Get services
            var aggregationService = appConfig.aggregationService();
            var sortingService = appConfig.sortingService();
            var filterService = appConfig.filterService();
            var reportService = appConfig.reportService();

            // Execute tasks
            var regionSummaries = aggregationService.aggregateSalesByRegion();
            var topSales = sortingService.getTopSales(appConfig.getProperties().getTopSalesCount());
            var filteredSales = filterService.filterByProduct(appConfig.getProperties().getFilterProductId());

            // Generate report
            reportService.generateReport(regionSummaries, topSales, filteredSales);

            System.out.println("Report generation completed successfully.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}