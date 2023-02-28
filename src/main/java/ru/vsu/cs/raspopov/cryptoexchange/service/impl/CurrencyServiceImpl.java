package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.*;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.repository.AmountOfUserCurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.CurrencyService;
import ru.vsu.cs.raspopov.cryptoexchange.utils.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final AmountOfUserCurrencyRepository amountOfUserCurrencyRepository;

    //TODO: сделать грамотное отображение double числа
    @Override
    public List<CurrencyDto.Response.CurrencyExchange> getExchangeRate(
            CurrencyDto.Request.SecretKeyCurrency currencyDto) {
        ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(currencyDto.getSecretKey())));

        Currency baseCurrency = ValidationUtil.validCurrency(currencyRepository.findByName(
                currencyDto.getCurrency()));

        List<CurrencyDto.Response.CurrencyExchange> exchangeRates = new ArrayList<>();
        exchangeRateRepository.findAllByBaseCurrency(baseCurrency).forEach(exchangeRate -> {
            exchangeRates.add(new CurrencyDto.Response.CurrencyExchange(
                    exchangeRate.getAnotherCurrency().getName(),
                    exchangeRate.getExchangeRate()));
        });

        log.info("User/Admin successfully get exchange rates");

        return exchangeRates;
    }

    @Override
    public List<CurrencyDto.Response.CurrencyExchange> updateExchangeRates(
            CurrencyDto.Request.ChangeExchangeRate currencyDto) {
        User user = ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(currencyDto.getSecretKey())));
        ValidationUtil.validUserRole(user, Role.ADMIN);

        Currency baseCurrency = ValidationUtil.validCurrency(currencyRepository.findByName(
                currencyDto.getCurrency()));

        List<CurrencyDto.Response.CurrencyExchange> currencies = currencyDto.getCurrencies();
        currencies.forEach(currencyExchange -> {
            ValidationUtil.validCurrency(currencyRepository.findByName(currencyExchange.getCurrency()));
        });

        return updateExchangeRates(currencies, baseCurrency);
    }

    private List<CurrencyDto.Response.CurrencyExchange> updateExchangeRates(
            List<CurrencyDto.Response.CurrencyExchange> currencies, Currency baseCurrency) {
        List<CurrencyDto.Response.CurrencyExchange> baseCurrencyExchangeRates = new ArrayList<>();
        currencies.forEach(currencyExchange -> {
            var anotherCurrency = currencyRepository.findByName(currencyExchange.getCurrency()).get();

            var exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(baseCurrency,
                    anotherCurrency).get();
            exchangeRate.setExchangeRate(currencyExchange.getExchangeRate());
            exchangeRateRepository.save(exchangeRate);

            baseCurrencyExchangeRates.add(new CurrencyDto.Response.CurrencyExchange(anotherCurrency.getName(),
                    currencyExchange.getExchangeRate()));

            exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(anotherCurrency,
                    baseCurrency).get();
            exchangeRate.setExchangeRate(1 / currencyExchange.getExchangeRate());
            exchangeRateRepository.save(exchangeRate);
        });

        log.info("ADMIN successfully update exchange rates");

        return baseCurrencyExchangeRates;
    }

    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount getTotalAmountOfCurrency(
            CurrencyDto.Request.SecretKeyCurrency currencyDto) {
        User user = ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(currencyDto.getSecretKey()))
        );
        ValidationUtil.validUserRole(user, Role.ADMIN);

        Currency baseCurrency = ValidationUtil.validCurrency(currencyRepository.findByName(
                currencyDto.getCurrency()));

        double totalCurrencyAmount = 0;
        for (AmountOfUserCurrency amountOfUserCurrency : amountOfUserCurrencyRepository
                .findAllByCurrency(baseCurrency)) {
            totalCurrencyAmount += amountOfUserCurrency.getAmount();
        }

        log.info("ADMIN successfully get total amount of currency");

        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(currencyDto.getCurrency(),
                totalCurrencyAmount);
    }
}
