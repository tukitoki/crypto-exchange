package ru.vsu.cs.raspopov.cryptoexchange.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrencyId;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface AmountOfUserCurrencyRepository extends CrudRepository<AmountOfUserCurrency, AmountOfUserCurrencyId> {

    Optional<AmountOfUserCurrency> findByCurrency(Currency currency);

    List<AmountOfUserCurrency> findAllByCurrency(Currency currency);

}
