package com.mockgen.service;

import com.mockgen.dao.SalesDao;
import com.mockgen.dao.DataAccessException;
import com.mockgen.model.Transaction;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для фильтрации транзакций продаж по различным критериям.
 * Содержит бизнес-логику для запросов транзакций по продукту, региону и сумме.
 */
public class SalesFilterService {
    private final SalesDao salesDao;

    /**
     * Создает экземпляр SalesFilterService с указанным объектом доступа к данным.
     *
     * @param salesDao объект доступа к данным для получения транзакций продаж
     */
    public SalesFilterService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    /**
     * Фильтрует транзакции по идентификатору продукта.
     * Возвращает все транзакции, соответствующие указанному идентификатору продукта.
     *
     * @param productId идентификатор продукта для фильтрации (с учетом регистра)
     * @return список транзакций для указанного продукта
     * @throws DataAccessException если произошла ошибка при доступе к данным о продажах
     *
     * @see Transaction
     */
    public List<Transaction> filterByProduct(String productId) throws DataAccessException {
        return salesDao.getAllTransactions().stream()
                .filter(t -> productId.equals(t.getProductId()))
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует транзакции по региону.
     * Возвращает все транзакции, соответствующие указанному региону.
     *
     * @param region регион для фильтрации (с учетом регистра)
     * @return список транзакций для указанного региона
     * @throws DataAccessException если произошла ошибка при доступе к данным о продажах
     *
     * @see Transaction
     */
    public List<Transaction> filterByRegion(String region) throws DataAccessException {
        return salesDao.getAllTransactions().stream()
                .filter(t -> region.equals(t.getRegion()))
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует транзакции по минимальной сумме.
     * Возвращает все транзакции с суммой больше указанного минимума.
     *
     * @param minAmount минимальный порог суммы (исключительно)
     * @return список транзакций с суммой больше minAmount
     * @throws DataAccessException если произошла ошибка при доступе к данным о продажах
     *
     * @see Transaction
     */
    public List<Transaction> filterByAmountGreaterThan(double minAmount) throws DataAccessException {
        return salesDao.getAllTransactions().stream()
                .filter(t -> t.getAmount().doubleValue() > minAmount)
                .collect(Collectors.toList());
    }
}