package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.ExchangeRate;
import ru.vsu.cs.raspopov.cryptoexchange.repository.AmountOfUserCurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.CurrencyService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final AmountOfUserCurrencyRepository amountOfUserCurrencyRepository;

    //TODO: сделать грамотное отображение double числа
    @Override
    public List<CurrencyDto.Response.CurrencyExchange> getExchangeRate(
            CurrencyDto.Request.SecretKeyCurrency currencyDto) {
        if (userRepository.findById(currencyDto.getSecretKey()).isEmpty()) {
            throw new NoSuchElementException("wrong secret_key");
        }
        if (currencyRepository.findByName(currencyDto.getName()).isEmpty()) {
            throw new NoSuchElementException("wrong currency");
        }

        Currency baseCurrency = currencyRepository.findByName(currencyDto.getName()).get();

        List<CurrencyDto.Response.CurrencyExchange> exchangeRates = new ArrayList<>();
        exchangeRateRepository.findAllByBaseCurrency(baseCurrency).forEach(exchangeRate -> {
            exchangeRates.add(new CurrencyDto.Response.CurrencyExchange(
                    exchangeRate.getAnotherCurrency().getName(),
                    exchangeRate.getExchangeRate()));
        });

        return exchangeRates;
    }

    @Override
    public List<CurrencyDto.Response.CurrencyExchange> updateExchangeRates(
            CurrencyDto.Request.ChangeExchangeRate currencyDto) {
        if (userRepository.findById(currencyDto.getSecretKey()).isEmpty()) {
            throw new NoSuchElementException("wrong secret_key");
        }
        if (currencyRepository.findByName(currencyDto.getName()).isEmpty()) {
            throw new NoSuchElementException("wrong currency");
        }

        List<CurrencyDto.Response.CurrencyExchange> currencies = currencyDto.getCurrencies();
        currencies.forEach(currencyExchange -> {
            if (currencyRepository.findByName(currencyExchange.getName()).isEmpty()) {
                throw new NoSuchElementException("wrong currency");
            }
        });

        Currency baseCurrency = currencyRepository.findByName(currencyDto.getName()).get();

        return updateExchangeRates(currencies, baseCurrency);
    }

    private List<CurrencyDto.Response.CurrencyExchange> updateExchangeRates(
            List<CurrencyDto.Response.CurrencyExchange> currencies, Currency baseCurrency) {
        List<CurrencyDto.Response.CurrencyExchange> baseCurrencyExchangeRates = new ArrayList<>();
        currencies.forEach(currencyExchange -> {
            Currency anotherCurrency = currencyRepository.findByName(currencyExchange.getName()).get();

            ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(baseCurrency,
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
        return baseCurrencyExchangeRates;
    }

    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount getTotalAmountOfCurrency(
            CurrencyDto.Request.SecretKeyCurrency currencyDto) {
        if (userRepository.findById(currencyDto.getSecretKey()).isEmpty()) {
            throw new NoSuchElementException("wrong secret_key");
        }
        if (currencyRepository.findByName(currencyDto.getName()).isEmpty()) {
            throw new NoSuchElementException("wrong currency");
        }

        Currency baseCurrency = currencyRepository.findByName(currencyDto.getName()).get();

        double totalCurrencyAmount = 0;
        for (AmountOfUserCurrency amountOfUserCurrency : amountOfUserCurrencyRepository.findAll()) {
            totalCurrencyAmount += amountOfUserCurrency.getAmount();
        }

        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(currencyDto.getName(), totalCurrencyAmount);
    }
}
