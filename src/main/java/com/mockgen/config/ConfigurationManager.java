package com.mockgen.config;

import com.mockgen.model.AppProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {

    public AppProperties loadProperties(String propertiesPath) throws IOException {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(propertiesPath)) {
            if (input == null) {
                throw new IOException("Properties file not found: " + propertiesPath);
            }
            props.load(input);
        }

        AppProperties appProperties = new AppProperties();
        appProperties.setCsvInputPath(props.getProperty("csv.input.path"));
        appProperties.setFilterProductId(props.getProperty("filter.product.id"));
        appProperties.setTopSalesCount(Integer.parseInt(
                props.getProperty("report.top.sales.count", "10")));
        appProperties.setCsvSeparator(
                props.getProperty("csv.separator", ",").charAt(0));
        appProperties.setReportOutput(props.getProperty("report.output", "STDOUT"));

        return appProperties;
    }
}