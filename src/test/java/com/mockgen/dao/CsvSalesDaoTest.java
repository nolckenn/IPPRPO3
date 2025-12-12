package com.mockgen.dao;

import com.mockgen.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvSalesDaoTest {
    @TempDir
    Path tempDir;

    private File testCsvFile;
    private CsvSalesDao csvSalesDao;

    @BeforeEach
    void setUp() throws IOException {
        testCsvFile = new File(tempDir.toFile(), "test-sales.csv");

        try (FileWriter writer = new FileWriter(testCsvFile)) {
            writer.write("TransactionID,Date,ProductID,Amount,Region\n");
            writer.write("1001,2025-10-10,P-123,150.00,North\n");
            writer.write("1002,2025-10-10,P-456,20.50,South\n");
            writer.write("1003,2025-10-11,P-123,150.00,West\n");
            writer.write("1004,2025-10-12,P-789,300.00,North\n");
        }

        csvSalesDao = new CsvSalesDao(testCsvFile.getAbsolutePath(), ',');
    }

    @Test
    void testGetAllTransactions() throws DataAccessException {
        List<Transaction> transactions = csvSalesDao.getAllTransactions();

        assertEquals(4, transactions.size());
        assertEquals("1001", transactions.get(0).getTransactionId());
        assertEquals("P-123", transactions.get(0).getProductId());
        assertEquals(new BigDecimal("150.00"), transactions.get(0).getAmount());
        assertEquals("North", transactions.get(0).getRegion());
    }

    @Test
    void testGetTransactionsByProduct() throws DataAccessException {
        List<Transaction> transactions = csvSalesDao.getTransactionsByProduct("P-123");

        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().allMatch(t -> "P-123".equals(t.getProductId())));
    }

    @Test
    void testGetTopTransactionsByAmount() throws DataAccessException {
        List<Transaction> topTransactions = csvSalesDao.getTopTransactionsByAmount(2);

        assertEquals(2, topTransactions.size());
        assertEquals("1004", topTransactions.get(0).getTransactionId());
        assertEquals("1001", topTransactions.get(1).getTransactionId());
    }

    @Test
    void testEmptyFile() throws IOException, DataAccessException {
        File emptyFile = new File(tempDir.toFile(), "empty.csv");
        try (FileWriter writer = new FileWriter(emptyFile)) {
            writer.write("TransactionID,Date,ProductID,Amount,Region\n");
        }

        CsvSalesDao emptyDao = new CsvSalesDao(emptyFile.getAbsolutePath(), ',');
        List<Transaction> transactions = emptyDao.getAllTransactions();

        assertTrue(transactions.isEmpty());
    }

    @Test
    void testInvalidCsvLine() throws IOException, DataAccessException {
        File invalidFile = new File(tempDir.toFile(), "invalid.csv");
        try (FileWriter writer = new FileWriter(invalidFile)) {
            writer.write("TransactionID,Date,ProductID,Amount,Region\n");
            writer.write("1001,2025-10-10,P-123,INVALID,North\n"); // Неверный формат суммы
            writer.write("1002,2025-10-10,P-456,20.50,South\n"); // Корректная строка
        }

        CsvSalesDao invalidDao = new CsvSalesDao(invalidFile.getAbsolutePath(), ',');
        List<Transaction> transactions = invalidDao.getAllTransactions();

        assertEquals(1, transactions.size()); // Только корректная строка должна быть обработана
        assertEquals("1002", transactions.get(0).getTransactionId());
    }
}