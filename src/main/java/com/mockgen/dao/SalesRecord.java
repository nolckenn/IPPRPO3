package com.mockgen.dao;

import com.mockgen.model.Transaction;
import java.util.List;

public interface SalesRecord {
    List<Transaction> getAllTransactions() throws DataAccessException;
}