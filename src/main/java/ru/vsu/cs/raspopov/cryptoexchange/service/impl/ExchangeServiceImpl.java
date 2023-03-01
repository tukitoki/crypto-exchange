package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.ExchangeCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.ExchangeService;
import ru.vsu.cs.raspopov.cryptoexchange.utils.ValidationUtil;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public List<ExchangeCurrencyDto.Response.CurrencyExchange> getExchangeRate(
            ExchangeCurrencyDto.Request.SecretKeyCurrency exchangeDto) {
        ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(exchangeDto.getSecretKey())));

        Currency baseCurrency = ValidationUtil.validCurrency(currencyRepository.findByName(
                exchangeDto.getCurrency()));

        List<ExchangeCurrencyDto.Response.CurrencyExchange> exchangeRates = new ArrayList<>();
        exchangeRateRepository.findAllByBaseCurrency(baseCurrency).forEach(exchangeRate -> {
            exchangeRates.add(new ExchangeCurrencyDto.Response.CurrencyExchange(
                    exchangeRate.getAnotherCurrency().getName(),
                    exchangeRate.getExchangeRate()));
        });

        log.info("User/Admin successfully get exchange rates");

        return exchangeRates;
    }

    @Transactional
    @Override
    public List<ExchangeCurrencyDto.Response.CurrencyExchange> updateExchangeRates(
            ExchangeCurrencyDto.Request.ChangeExchangeRate exchangeDto) {
        User user = ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(exchangeDto.getSecretKey())));
        ValidationUtil.validUserRole(user, Role.ADMIN);

        Currency baseCurrency = ValidationUtil.validCurrency(currencyRepository.findByName(
                exchangeDto.getCurrency()));

        List<ExchangeCurrencyDto.Response.CurrencyExchange> currencies = exchangeDto.getCurrencies();
        currencies.forEach(currencyExchange -> {
            ValidationUtil.validCurrency(currencyRepository.findByName(currencyExchange.getCurrency()));
        });

        return updateExchangeRates(currencies, baseCurrency);
    }

    private List<ExchangeCurrencyDto.Response.CurrencyExchange> updateExchangeRates(
            List<ExchangeCurrencyDto.Response.CurrencyExchange> currencies, Currency baseCurrency) {
        List<ExchangeCurrencyDto.Response.CurrencyExchange> baseCurrencyExchangeRates = new ArrayList<>();
        currencies.forEach(currencyExchange -> {
            var anotherCurrency = currencyRepository.findByName(currencyExchange.getCurrency()).get();

            var exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(baseCurrency,
                    anotherCurrency).get();
            exchangeRate.setExchangeRate(currencyExchange.getExchangeRate());
            exchangeRateRepository.save(exchangeRate);

            baseCurrencyExchangeRates.add(new ExchangeCurrencyDto.Response.CurrencyExchange(anotherCurrency.getName(),
                    currencyExchange.getExchangeRate()));

            exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(anotherCurrency,
                    baseCurrency).get();
            exchangeRate.setExchangeRate(BigDecimal.ONE.divide(currencyExchange.getExchangeRate(),
                    5, RoundingMode.HALF_DOWN));
            exchangeRateRepository.save(exchangeRate);
        });

        log.info("ADMIN successfully update exchange rates");

        return baseCurrencyExchangeRates;
    }
}
