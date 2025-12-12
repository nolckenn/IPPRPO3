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
class SalesSortingServiceTest {
    @Mock
    private SalesDao salesDao;

    private SalesSortingService sortingService;

    @BeforeEach
    void setUp() {
        sortingService = new SalesSortingService(salesDao);
    }

    @Test
    void testGetTopSales() throws DataAccessException {
        List<Transaction> allTransactions = Arrays.asList(
                new Transaction("1001", LocalDate.of(2025, 10, 10), "P-123",
                        new BigDecimal("150.00"), "North"),
                new Transaction("1002", LocalDate.of(2025, 10, 10), "P-456",
                        new BigDecimal("300.00"), "South"),
                new Transaction("1003", LocalDate.of(2025, 10, 11), "P-789",
                        new BigDecimal("50.00"), "West"),
                new Transaction("1004", LocalDate.of(2025, 10, 12), "P-999",
                        new BigDecimal("200.00"), "North")
        );

        when(salesDao.getTopTransactionsByAmount(3)).thenReturn(Arrays.asList(
                allTransactions.get(1), // 300.00
                allTransactions.get(3), // 200.00
                allTransactions.get(0)  // 150.00
        ));

        List<Transaction> topSales = sortingService.getTopSales(3);

        assertEquals(3, topSales.size());
        assertEquals("1002", topSales.get(0).getTransactionId());
        assertEquals("1004", topSales.get(1).getTransactionId());
        assertEquals("1001", topSales.get(2).getTransactionId());

        assertTrue(topSales.get(0).getAmount().compareTo(topSales.get(1).getAmount()) > 0);
        assertTrue(topSales.get(1).getAmount().compareTo(topSales.get(2).getAmount()) > 0);

        verify(salesDao, times(1)).getTopTransactionsByAmount(3);
    }

    @Test
    void testGetTopSalesLessThanRequested() throws DataAccessException {
        List<Transaction> allTransactions = Arrays.asList(
                new Transaction("1001", LocalDate.of(2025, 10, 10), "P-123",
                        new BigDecimal("150.00"), "North"),
                new Transaction("1002", LocalDate.of(2025, 10, 10), "P-456",
                        new BigDecimal("20.50"), "South")
        );

        when(salesDao.getTopTransactionsByAmount(10)).thenReturn(allTransactions);

        List<Transaction> topSales = sortingService.getTopSales(10);

        assertEquals(2, topSales.size()); // Запрашиваем 10, но есть только 2
        verify(salesDao, times(1)).getTopTransactionsByAmount(10);
    }

    @Test
    void testGetTopSalesEmptyList() throws DataAccessException {
        when(salesDao.getTopTransactionsByAmount(5)).thenReturn(Arrays.asList());

        List<Transaction> topSales = sortingService.getTopSales(5);

        assertTrue(topSales.isEmpty());
        verify(salesDao, times(1)).getTopTransactionsByAmount(5);
    }
}