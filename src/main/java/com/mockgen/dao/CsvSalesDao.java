package com.mockgen.dao;

import com.mockgen.model.Transaction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CsvSalesDao implements SalesDao {
    private final String filePath;
    private final char separator;
    private final DateTimeFormatter dateFormatter;

    public CsvSalesDao(String filePath, char separator) {
        this.filePath = filePath;
        this.separator = separator;
        this.dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    }

    @Override
    public List<Transaction> getAllTransactions() throws DataAccessException {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                Transaction transaction = parseTransaction(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Error reading CSV file: " + filePath, e);
        }

        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByProduct(String productId) throws DataAccessException {
        return getAllTransactions().stream()
                .filter(t -> t.getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getTopTransactionsByAmount(int count) throws DataAccessException {
        return getAllTransactions().stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Transaction parseTransaction(String line) {
        String[] parts = line.split(String.valueOf(separator));

        if (parts.length != 5) {
            return null;
        }

        try {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(parts[0].trim());
            transaction.setDate(LocalDate.parse(parts[1].trim(), dateFormatter));
            transaction.setProductId(parts[2].trim());
            transaction.setAmount(new BigDecimal(parts[3].trim()));
            transaction.setRegion(parts[4].trim());
            return transaction;
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            return null;
        }
    }
}