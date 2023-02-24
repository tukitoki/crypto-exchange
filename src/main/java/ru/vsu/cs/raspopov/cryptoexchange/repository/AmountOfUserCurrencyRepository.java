package ru.vsu.cs.raspopov.cryptoexchange.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrencyId;

public interface AmountOfUserCurrencyRepository extends CrudRepository<AmountOfUserCurrency, AmountOfUserCurrencyId> {
}
