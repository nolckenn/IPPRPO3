package com.mockgen.service;

import com.mockgen.dao.SalesDao;
import com.mockgen.dao.DataAccessException;
import com.mockgen.model.RegionSummary;
import com.mockgen.model.Transaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для агрегации данных о продажах.
 * Содержит бизнес-логику для расчета суммарных продаж по регионам.
 */
public class SalesAggregationService {
    private final SalesDao salesDao;

    /**
     * Создает экземпляр SalesAggregationService с указанным объектом доступа к данным.
     *
     * @param salesDao объект доступа к данным для получения транзакций продаж
     */
    public SalesAggregationService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    /**
     * Агрегирует данные о продажах по регионам, вычисляя общую сумму продаж для каждого региона.
     * Метод группирует транзакции по региону и суммирует суммы транзакций в одном регионе.
     *
     * @return список объектов RegionSummary, содержащих названия регионов и их общие суммы продаж
     * @throws DataAccessException если произошла ошибка при доступе к данным о продажах
     *
     * @see RegionSummary
     * @see Transaction
     */
    public List<RegionSummary> aggregateSalesByRegion() throws DataAccessException {
        List<Transaction> transactions = salesDao.getAllTransactions();

        Map<String, BigDecimal> regionTotals = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getRegion,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));

        return regionTotals.entrySet().stream()
                .map(entry -> new RegionSummary(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}