package com.mockgen.dao;

import com.mockgen.model.Transaction;
import java.util.List;

public interface SalesDao extends SalesRecord {
    List<Transaction> getTransactionsByProduct(String productId) throws DataAccessException;
    List<Transaction> getTopTransactionsByAmount(int count) throws DataAccessException;
}