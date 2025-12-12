package com.mockgen.service;

import com.mockgen.dao.SalesDao;
import com.mockgen.dao.DataAccessException;
import com.mockgen.model.RegionSummary;
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
class SalesAggregationServiceTest {
    @Mock
    private SalesDao salesDao;

    private SalesAggregationService aggregationService;

    @BeforeEach
    void setUp() {
        aggregationService = new SalesAggregationService(salesDao);
    }

    @Test
    void testAggregateSalesByRegion() throws DataAccessException {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("1001", LocalDate.of(2025, 10, 10), "P-123",
                        new BigDecimal("150.00"), "North"),
                new Transaction("1002", LocalDate.of(2025, 10, 10), "P-456",
                        new BigDecimal("20.50"), "South"),
                new Transaction("1003", LocalDate.of(2025, 10, 11), "P-123",
                        new BigDecimal("150.00"), "West"),
                new Transaction("1004", LocalDate.of(2025, 10, 12), "P-789",
                        new BigDecimal("300.00"), "North")
        );

        when(salesDao.getAllTransactions()).thenReturn(transactions);

        List<RegionSummary> summaries = aggregationService.aggregateSalesByRegion();

        assertEquals(3, summaries.size());

        RegionSummary northSummary = summaries.stream()
                .filter(s -> "North".equals(s.getRegion()))
                .findFirst()
                .orElseThrow();
        assertEquals(new BigDecimal("450.00"), northSummary.getTotalAmount());

        RegionSummary southSummary = summaries.stream()
                .filter(s -> "South".equals(s.getRegion()))
                .findFirst()
                .orElseThrow();
        assertEquals(new BigDecimal("20.50"), southSummary.getTotalAmount());

        RegionSummary westSummary = summaries.stream()
                .filter(s -> "West".equals(s.getRegion()))
                .findFirst()
                .orElseThrow();
        assertEquals(new BigDecimal("150.00"), westSummary.getTotalAmount());

        verify(salesDao, times(1)).getAllTransactions();
    }

    @Test
    void testAggregateEmptySales() throws DataAccessException {
        when(salesDao.getAllTransactions()).thenReturn(Arrays.asList());

        List<RegionSummary> summaries = aggregationService.aggregateSalesByRegion();

        assertTrue(summaries.isEmpty());
        verify(salesDao, times(1)).getAllTransactions();
    }

    @Test
    void testAggregateSingleTransaction() throws DataAccessException {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("1001", LocalDate.of(2025, 10, 10), "P-123",
                        new BigDecimal("150.00"), "North")
        );

        when(salesDao.getAllTransactions()).thenReturn(transactions);

        List<RegionSummary> summaries = aggregationService.aggregateSalesByRegion();

        assertEquals(1, summaries.size());
        assertEquals("North", summaries.get(0).getRegion());
        assertEquals(new BigDecimal("150.00"), summaries.get(0).getTotalAmount());
    }
}