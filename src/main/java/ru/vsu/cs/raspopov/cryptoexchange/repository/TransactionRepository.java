package ru.vsu.cs.raspopov.cryptoexchange.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Transaction;

import java.sql.Timestamp;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query(value = "SELECT COUNT(*) FROM Transaction t WHERE t.date >= ?1 and t.date <= ?2")
    Integer getCountTransactionInTime(Timestamp from, Timestamp to);
}
