package com.mockgen.service;

import com.mockgen.dao.SalesDao;
import com.mockgen.dao.DataAccessException;
import com.mockgen.model.Transaction;
import java.util.List;

/**
 * Сервис для сортировки и ранжирования транзакций продаж.
 * Содержит бизнес-логику для получения топовых транзакций продаж.
 */
public class SalesSortingService {
    private final SalesDao salesDao;

    /**
     * Создает экземпляр SalesSortingService с указанным объектом доступа к данным.
     *
     * @param salesDao объект доступа к данным для получения транзакций продаж
     */
    public SalesSortingService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    /**
     * Получает топ N транзакций продаж, отсортированных по сумме в порядке убывания.
     * Транзакции с наибольшими суммами возвращаются первыми.
     * Если доступно меньше транзакций, чем запрошено, возвращаются все доступные
     * транзакции в отсортированном порядке.
     *
     * @param count максимальное количество топовых транзакций для возврата
     * @return список топ N транзакций, отсортированных по сумме (наибольшие сначала)
     * @throws DataAccessException если произошла ошибка при доступе к данным о продажах
     * @throws IllegalArgumentException если count отрицательный
     *
     * @see Transaction
     */
    public List<Transaction> getTopSales(int count) throws DataAccessException {
        if (count < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным: " + count);
        }
        return salesDao.getTopTransactionsByAmount(count);
    }
}