package com.mockgen.config;

import com.mockgen.dao.CsvSalesDao;
import com.mockgen.dao.SalesDao;
import com.mockgen.model.AppProperties;
import com.mockgen.service.*;

public class AppConfig {
    private final AppProperties properties;
    private final ConfigurationManager configManager;

    public AppConfig() {
        this.configManager = new ConfigurationManager();
        this.properties = loadProperties();
    }

    private AppProperties loadProperties() {
        try {
            return configManager.loadProperties("app.properties");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public AppProperties getProperties() {
        return properties;
    }

    public SalesDao salesDao() {
        return new CsvSalesDao(
                properties.getCsvInputPath(),
                properties.getCsvSeparator()
        );
    }

    public SalesAggregationService aggregationService() {
        return new SalesAggregationService(salesDao());
    }

    public SalesFilterService filterService() {
        return new SalesFilterService(salesDao());
    }

    public SalesSortingService sortingService() {
        return new SalesSortingService(salesDao());
    }

    public ReportService reportService() {
        return new ReportService(properties);
    }
}