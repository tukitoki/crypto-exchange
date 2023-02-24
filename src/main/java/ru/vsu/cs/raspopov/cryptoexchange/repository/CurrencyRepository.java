package ru.vsu.cs.raspopov.cryptoexchange.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;

import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    Optional<Currency> findByName(String name);
}
