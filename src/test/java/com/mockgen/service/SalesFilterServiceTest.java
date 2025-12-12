package com.mockgen.service;

import com.mockgen.dao.SalesDao;
import com.mockgen.dao.DataAccessException;
import com.mockgen.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesFilterServiceTest {
    @Mock
    private SalesDao salesDao;

    private SalesFilterService filterService;

    @BeforeEach
    void setUp() {
        filterService = new SalesFilterService(salesDao);
    }

    @Test
    void testFilterByProduct() throws DataAccessException {
        List<Transaction> allTransactions = Arrays.asList(
                new Transaction("1001", LocalDate.of(2025, 10, 10), "P-123",
                        new BigDecimal("150.00"), "North"),
                new Transaction("1002", LocalDate.of(2025, 10, 10), "P-456",
                        new BigDecimal("20.50"), "South"),
                new Transaction("1003", LocalDate.of(2025, 10, 11), "P-123",
                        new BigDecimal("150.00"), "West")
        );

        when(salesDao.getAllTransactions()).thenReturn(allTransactions);

        List<Transaction> filtered = filterService.filterByProduct("P-123");

        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(t -> "P-123".equals(t.getProductId())));
        assertEquals("1001", filtered.get(0).getTransactionId());
        assertEquals("1003", filtered.get(1).getTransactionId());

        verify(salesDao, times(1)).getAllTransactions();
    }

    @Test
    void testFilterByProductNoMatches() throws DataAccessException {
        List<Transaction> allTransactions = Arrays.asList(
                new Transaction("1001", LocalDate.of(2025, 10, 10), "P-123",
                        new BigDecimal("150.00"), "North"),
                new Transaction("1002", LocalDate.of(2025, 10, 10), "P-456",
                        new BigDecimal("20.50"), "South")
        );

        when(salesDao.getAllTransactions()).thenReturn(allTransactions);

        List<Transaction> filtered = filterService.filterByProduct("P-999");

        assertTrue(filtered.isEmpty());
        verify(salesDao, times(1)).getAllTransactions();
    }

    @Test
    void testFilterByRegion() throws DataAccessException {
        List<Transaction> allTransactions = Arrays.asList(
                new Transaction("1001", LocalDate.of(2025, 10, 10), "P-123",
                        new BigDecimal("150.00"), "North"),
                new Transaction("1002", LocalDate.of(2025, 10, 10), "P-456",
                        new BigDecimal("20.50"), "South"),
                new Transaction("1003", LocalDate.of(2025, 10, 11), "P-123",
                        new BigDecimal("150.00"), "North")
        );

        when(salesDao.getAllTransactions()).thenReturn(allTransactions);

        List<Transaction> filtered = filterService.filterByRegion("North");

        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(t -> "North".equals(t.getRegion())));
        verify(salesDao, times(1)).getAllTransactions();
    }

    @Test
    void testFilterByAmountGreaterThan() throws DataAccessException {
        List<Transaction> allTransactions = Arrays.asList(
                new Transaction("1001", LocalDate.of(2025, 10, 10), "P-123",
                        new BigDecimal("150.00"), "North"),
                new Transaction("1002", LocalDate.of(2025, 10, 10), "P-456",
                        new BigDecimal("20.50"), "South"),
                new Transaction("1003", LocalDate.of(2025, 10, 11), "P-789",
                        new BigDecimal("300.00"), "West")
        );

        when(salesDao.getAllTransactions()).thenReturn(allTransactions);

        List<Transaction> filtered = filterService.filterByAmountGreaterThan(100.0);

        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(t -> t.getAmount().doubleValue() > 100.0));
        verify(salesDao, times(1)).getAllTransactions();
    }
}