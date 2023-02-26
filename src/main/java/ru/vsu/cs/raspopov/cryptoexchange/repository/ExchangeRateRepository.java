package ru.vsu.cs.raspopov.cryptoexchange.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Integer> {

    List<ExchangeRate> findAllByBaseCurrency(Currency baseCurrency);

    Optional<ExchangeRate> findByBaseCurrencyAndAnotherCurrency(Currency baseCurrency, Currency anotherCurrency);
}
