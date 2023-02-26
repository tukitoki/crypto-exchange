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
    public List<CurrencyDto.Response.CurrencyExchangeDto> getExchangeRate(
            CurrencyDto.Request.SecretKeyCurrencyDto currencyDto) {
        if (userRepository.findById(currencyDto.getSecretKey()).isEmpty()) {
            throw new NoSuchElementException("wrong secret_key");
        }
        if (currencyRepository.findByName(currencyDto.getName()).isEmpty()) {
            throw new NoSuchElementException("wrong currency");
        }

        Currency baseCurrency = currencyRepository.findByName(currencyDto.getName()).get();

        List<CurrencyDto.Response.CurrencyExchangeDto> exchangeRates = new ArrayList<>();
        exchangeRateRepository.findAllByBaseCurrency(baseCurrency).forEach(exchangeRate -> {
            exchangeRates.add(new CurrencyDto.Response.CurrencyExchangeDto(
                    exchangeRate.getAnotherCurrency().getName(),
                    exchangeRate.getExchangeRate()));
        });

        return exchangeRates;
    }

    @Override
    public List<CurrencyDto.Response.CurrencyExchangeDto> updateExchangeRates(
            CurrencyDto.Request.ChangeExchangeRateDto currencyDto) {
        if (userRepository.findById(currencyDto.getSecretKey()).isEmpty()) {
            throw new NoSuchElementException("wrong secret_key");
        }
        if (currencyRepository.findByName(currencyDto.getName()).isEmpty()) {
            throw new NoSuchElementException("wrong currency");
        }

        List<CurrencyDto.Response.CurrencyExchangeDto> currencies = currencyDto.getCurrencies();
        currencies.forEach(currencyExchangeDto -> {
            if (currencyRepository.findByName(currencyExchangeDto.getName()).isEmpty()) {
                throw new NoSuchElementException("wrong currency");
            }
        });

        Currency baseCurrency = currencyRepository.findByName(currencyDto.getName()).get();

        return updateExchangeRates(currencies, baseCurrency);
    }

    private List<CurrencyDto.Response.CurrencyExchangeDto> updateExchangeRates(
            List<CurrencyDto.Response.CurrencyExchangeDto> currencies, Currency baseCurrency) {
        List<CurrencyDto.Response.CurrencyExchangeDto> baseCurrencyExchangeRates = new ArrayList<>();
        currencies.forEach(currencyExchangeDto -> {
            Currency anotherCurrency = currencyRepository.findByName(currencyExchangeDto.getName()).get();

            ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(baseCurrency,
                    anotherCurrency).get();
            exchangeRate.setExchangeRate(currencyExchangeDto.getExchangeRate());
            exchangeRateRepository.save(exchangeRate);

            baseCurrencyExchangeRates.add(new CurrencyDto.Response.CurrencyExchangeDto(anotherCurrency.getName(),
                    currencyExchangeDto.getExchangeRate()));

            exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(anotherCurrency,
                    baseCurrency).get();
            exchangeRate.setExchangeRate(1 / currencyExchangeDto.getExchangeRate());
            exchangeRateRepository.save(exchangeRate);
        });
        return baseCurrencyExchangeRates;
    }

    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount getTotalAmountOfCurrency(
            CurrencyDto.Request.SecretKeyCurrencyDto currencyDto) {
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
