package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.vsu.cs.raspopov.cryptoexchange.dto.ExchangeCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.ExchangeRate;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.ExchangeService;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ExchangeServiceImplTest {

    @Autowired
    private ExchangeService exchangeService;
    @MockBean
    private CurrencyRepository currencyRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    void getExchangeRate() {
        User user = new User(
                UUID.fromString("f24de643-ace3-4224-8534-681d6c329aca"),
                "test",
                "test@mail.ru",
                Role.USER,
                new ArrayList<>()
        );
        Currency currency = new Currency(
                0,
                "RUB",
                new ArrayList<>(),
                new ArrayList<>()
        );
        Currency anotherCurrency = new Currency(
                0,
                "TON",
                new ArrayList<>(),
                new ArrayList<>()
        );
        var exchangeRates = List.of(new ExchangeRate(1, currency, anotherCurrency, new BigDecimal("4.0")));
        when(userRepository.findById(user.getSecretKey())).thenReturn(Optional.of(user));
        when(currencyRepository.findByName(currency.getName())).thenReturn(Optional.of(currency));
        when(exchangeRateRepository.findAllByBaseCurrency(currency))
                .thenReturn(exchangeRates);
        var rates = exchangeService.getExchangeRate(new ExchangeCurrencyDto.Request.SecretKeyCurrency(
                user.getSecretKey().toString(),
                currency.getName()
        ));
        var expected = List.of(new ExchangeCurrencyDto.Response.CurrencyExchange("TON",
                new BigDecimal("4.0")));
        assertEquals(expected, rates);
    }

    @Test
    void getExchangeRate_ReturnsException() {
        User user = new User(
                UUID.fromString("f24de643-ace3-4224-8534-681d6c329aca"),
                "test",
                "test@mail.ru",
                Role.USER,
                new ArrayList<>()
        );
        Currency currency = new Currency(
                0,
                "EEE",
                new ArrayList<>(),
                new ArrayList<>()
        );
        Currency anotherCurrency = new Currency(
                0,
                "TON",
                new ArrayList<>(),
                new ArrayList<>()
        );
        var exchangeRates = List.of(new ExchangeRate(1, currency, anotherCurrency, new BigDecimal("4.0")));
        when(userRepository.findById(user.getSecretKey())).thenReturn(Optional.of(user));
        when(currencyRepository.findByName(currency.getName())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> exchangeService.getExchangeRate(new ExchangeCurrencyDto.Request.SecretKeyCurrency(
                        user.getSecretKey().toString(),
                        currency.getName()
                )));
    }
}