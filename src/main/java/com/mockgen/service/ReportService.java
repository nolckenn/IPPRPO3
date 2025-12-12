package com.mockgen.service;

import com.mockgen.model.AppProperties;
import com.mockgen.model.RegionSummary;
import com.mockgen.model.Transaction;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Сервис для генерации форматированных отчетов о продажах.
 * Содержит бизнес-логику для создания комплексных сводок по продажам
 * с несколькими разделами: итоги по регионам, топ продажи и отфильтрованные результаты.
 */
public class ReportService {
    private final AppProperties properties;

    /**
     * Создает экземпляр ReportService с указанными свойствами приложения.
     *
     * @param properties конфигурационные свойства приложения
     */
    public ReportService(AppProperties properties) {
        this.properties = properties;
    }

    /**
     * Генерирует комплексный отчет о продажах, содержащий три основных раздела:
     * 1. Продажи, агрегированные по регионам с суммарными суммами
     * 2. Топ N транзакций продаж (только по ID)
     * 3. Продажи, отфильтрованные по настроенному идентификатору продукта
     *
     * Направление вывода отчета определяется конфигурацией:
     * - "STDOUT": вывод в стандартный поток вывода
     * - Любое другое значение: запись в файл "sales-report.txt"
     *
     * @param regionSummaries список сводок по регионам
     * @param topSales список топовых транзакций продаж
     * @param filteredSales список продаж, отфильтрованных по продукту
     * @throws IOException если произошла ошибка при записи отчета в файл
     *
     * @see RegionSummary
     * @see Transaction
     * @see AppProperties
     */
    public void generateReport(List<RegionSummary> regionSummaries,
                               List<Transaction> topSales,
                               List<Transaction> filteredSales) throws IOException {

        StringBuilder report = new StringBuilder();

        // Раздел "Продажи по регионам"
        report.append("--- Sales by Region ---\n");
        for (RegionSummary summary : regionSummaries) {
            report.append(summary).append("\n");
        }
        report.append("\n");

        // Раздел "Топ продажи"
        report.append("--- Top ").append(properties.getTopSalesCount())
                .append(" Sales (ID) ---\n");
        for (Transaction transaction : topSales) {
            report.append(transaction.getTransactionId()).append("\n");
        }
        report.append("\n");

        // Раздел "Отфильтрованные продажи"
        report.append("--- Filtered by Product ").append(properties.getFilterProductId())
                .append(" ---\n");
        for (Transaction transaction : filteredSales) {
            report.append(transaction).append("\n");
        }

        // Определение направления вывода на основе конфигурации
        if ("STDOUT".equalsIgnoreCase(properties.getReportOutput())) {
            System.out.println(report.toString());
        } else {
            try (PrintWriter writer = new PrintWriter(new FileWriter("sales-report.txt"))) {
                writer.print(report.toString());
            }
        }
    }
}