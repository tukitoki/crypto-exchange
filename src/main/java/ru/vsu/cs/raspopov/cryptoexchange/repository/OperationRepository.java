package ru.vsu.cs.raspopov.cryptoexchange.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Transaction;

public interface OperationRepository extends CrudRepository<Transaction, Integer> {
}
