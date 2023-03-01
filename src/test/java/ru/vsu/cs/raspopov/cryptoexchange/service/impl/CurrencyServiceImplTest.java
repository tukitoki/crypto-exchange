package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.ExchangeRate;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.repository.AmountOfUserCurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.CurrencyService;

import java.nio.file.AccessDeniedException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CurrencyServiceImplTest {

    @Autowired
    private CurrencyService currencyService;
    @MockBean
    private CurrencyRepository currencyRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AmountOfUserCurrencyRepository amountOfUserCurrencyRepository;

    @Test
    void getTotalAmountOfCurrency() {
        User admin = new User(
                UUID.fromString("f24de143-ace3-4224-8534-681d6c329aca"),
                "admin",
                "admin@mail.ru",
                Role.ADMIN,
                new ArrayList<>()
        );
        User user = new User(
                UUID.fromString("f34de643-ace3-4224-8534-681d6c329aca"),
                "test",
                "test@mail.ru",
                Role.USER,
                new ArrayList<>()
        );
        User secondUser = new User(
                UUID.fromString("f24de623-ace3-4224-8534-681d6c329aca"),
                "test1",
                "test1@mail.ru",
                Role.USER,
                new ArrayList<>()
        );
        Currency baseCurrency = new Currency(
                0,
                "RUB",
                new ArrayList<>(),
                new ArrayList<>()
        );
        var amounts = List.of(
                new AmountOfUserCurrency(user, baseCurrency, 434.0),
                new AmountOfUserCurrency(secondUser, baseCurrency, 500.0)
        );
        Mockito.when(userRepository.findById(admin.getSecretKey())).thenReturn(Optional.of(admin));
        Mockito.when(currencyRepository.findByName(baseCurrency.getName())).thenReturn(Optional.of(baseCurrency));
        Mockito.when(amountOfUserCurrencyRepository.findAllByCurrency(baseCurrency))
                .thenReturn(amounts);
        var sumAmount = new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                "RUB",
                934.0
        );
        assertEquals(sumAmount, currencyService.getTotalAmountOfCurrency(
                        new CurrencyDto.Request.SecretKeyCurrency(
                                admin.getSecretKey().toString(),
                                baseCurrency.getName())
                )
        );
    }

    @Test
    void getTotalAmountOfCurrency_ReturnsException() {
        User admin = new User(
                UUID.fromString("f24de143-ace3-4224-8534-681d6c329aca"),
                "admin",
                "admin@mail.ru",
                Role.ADMIN,
                new ArrayList<>()
        );
        Mockito.when(userRepository.findById(admin.getSecretKey())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> currencyService.getTotalAmountOfCurrency(
                        new CurrencyDto.Request.SecretKeyCurrency(
                                admin.getSecretKey().toString(),
                                "RUB")
        ));
    }

    @Test
    void getTotalAmountOfCurrency_ReturnsAccessDeniedException() {
        User user = new User(
                UUID.fromString("f24de143-ace3-4224-8534-681d6c329aca"),
                "user",
                "user@mail.ru",
                Role.USER,
                new ArrayList<>()
        );
        Mockito.when(userRepository.findById(user.getSecretKey())).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class,
                () -> currencyService.getTotalAmountOfCurrency(
                        new CurrencyDto.Request.SecretKeyCurrency(
                                user.getSecretKey().toString(),
                                "RUB")
                ));
    }
}