package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.BalanceOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.*;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.TransactionType;
import ru.vsu.cs.raspopov.cryptoexchange.repository.AmountOfUserCurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.BalanceService;
import ru.vsu.cs.raspopov.cryptoexchange.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BalanceServiceImplTest {

    @Autowired
    BalanceService balanceService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CurrencyRepository currencyRepository;
    @MockBean
    private AmountOfUserCurrencyRepository amountOfUserCurrencyRepository;
    @MockBean
    private ExchangeRateRepository exchangeRateRepository;
    @MockBean
    private TransactionService transactionService;

    @Test
    void getUserBalance() {
        Currency currency = new Currency(1, "RUB", new ArrayList<>(), new ArrayList<>());
        User user = new User(
                UUID.fromString("f24de643-ace3-4224-8534-681d6c329aca"),
                "test",
                "test@mail.ru",
                Role.USER,
                new ArrayList<>()
        );
        user.getWallet().add(new AmountOfUserCurrency(user, currency, new BigDecimal("120")));
        Mockito.when(userRepository.findById(user.getSecretKey())).thenReturn(Optional.of(user));
        var userDto = new UserDto.Request.UserSecretKey();
        userDto.setSecretKey(user.getSecretKey().toString());
        var currencies = balanceService.getUserBalance(userDto);
        var expected = List.of(new AmountOfUserCurrencyDto.Response.CurrencyAmount("RUB",
                new BigDecimal("120")));
        assertEquals(expected, currencies);
    }

    @Test
    void replenishmentBalance() {
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
        var amountOfUserCurrency = new AmountOfUserCurrency(
                user, currency, new BigDecimal("200")
        );
        var amountOfCurrency = new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                "RUB", new BigDecimal("500")
        );
        Mockito.when(userRepository.findById(user.getSecretKey())).thenReturn(Optional.of(user));
        Mockito.when(currencyRepository.findByName(currency.getName())).thenReturn(Optional.of(currency));
        Mockito.when(amountOfUserCurrencyRepository.findById(new AmountOfUserCurrencyId(
                        user.getSecretKey(),
                        currency.getId())))
                .thenReturn(Optional.of(amountOfUserCurrency));
        Mockito.when(transactionService.saveTransaction(
                        TransactionType.REPLENISHMENT,
                        user.getSecretKey().toString()))
                .thenReturn(new TransactionDto.Response.Transaction(
                        user.getSecretKey().toString(),
                        LocalDate.now())
                );

        var currencyDto = balanceService.replenishmentBalance(
                new BalanceOperationDto.Request.ReplenishmentBalance(
                        user.getSecretKey().toString(),
                        "RUB",
                        new BigDecimal("300")
                ));
        assertEquals(currencyDto, amountOfCurrency);
    }

    @Test
    void withdrawalMoney() {
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
        var amountOfUserCurrency = new AmountOfUserCurrency(
                user, currency, new BigDecimal("200")
        );
        var amountOfCurrency = new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                "RUB", new BigDecimal("100")
        );

        Mockito.when(userRepository.findById(user.getSecretKey())).thenReturn(Optional.of(user));
        Mockito.when(currencyRepository.findByName(currency.getName())).thenReturn(Optional.of(currency));
        Mockito.when(amountOfUserCurrencyRepository.findById(new AmountOfUserCurrencyId(
                        user.getSecretKey(),
                        currency.getId())))
                .thenReturn(Optional.of(amountOfUserCurrency));
        Mockito.when(transactionService.saveTransaction(
                        TransactionType.REPLENISHMENT,
                        user.getSecretKey().toString()))
                .thenReturn(new TransactionDto.Response.Transaction(
                        user.getSecretKey().toString(),
                        LocalDate.now())
                );

        var currencyDto = balanceService.withdrawalMoney(
                new BalanceOperationDto.Request.WithdrawalBalance(
                        user.getSecretKey().toString(),
                        "RUB",
                        new BigDecimal("100"),
                        "card"
                ));

        assertEquals(currencyDto, amountOfCurrency);
    }

    @Test
    void exchangeCurrency() {
        User user = new User(
                UUID.fromString("f24de643-ace3-4224-8534-681d6c329aca"),
                "test",
                "test@mail.ru",
                Role.USER,
                new ArrayList<>()
        );
        Currency baseCurrency = new Currency(
                0,
                "RUB",
                new ArrayList<>(),
                new ArrayList<>()
        );
        Currency exchangeableCurrency = new Currency(
                1,
                "TON",
                new ArrayList<>(),
                new ArrayList<>()
        );

        var amountOfBaseCurrency = new AmountOfUserCurrency(
                user, baseCurrency, new BigDecimal("200")
        );
        var amountOfCurrency = new BalanceOperationDto.Response.ExchangeCurrency(
                "RUB", "RUB", new BigDecimal("200"), new BigDecimal("200")
        );
        var exchangeRate = new ExchangeRate(1, baseCurrency, exchangeableCurrency, new BigDecimal("1"));
        Mockito.when(userRepository.findById(user.getSecretKey())).thenReturn(Optional.of(user));
        Mockito.when(currencyRepository.findByName(baseCurrency.getName())).thenReturn(Optional.of(baseCurrency));
        Mockito.when(amountOfUserCurrencyRepository.findById(new AmountOfUserCurrencyId(
                        user.getSecretKey(),
                        baseCurrency.getId())))
                .thenReturn(Optional.of(amountOfBaseCurrency));
        Mockito.when(exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(baseCurrency, baseCurrency))
                .thenReturn(Optional.of(exchangeRate));
        Mockito.when(amountOfUserCurrencyRepository.save(amountOfBaseCurrency))
                .thenReturn(amountOfBaseCurrency);
        Mockito.when(transactionService.saveTransaction(
                        TransactionType.REPLENISHMENT,
                        user.getSecretKey().toString()))
                .thenReturn(new TransactionDto.Response.Transaction(
                        user.getSecretKey().toString(),
                        LocalDate.now())
                );

        var currencyDto = balanceService.exchangeCurrency(
                new BalanceOperationDto.Request.ExchangeCurrency(
                        user.getSecretKey().toString(),
                        "RUB",
                        "RUB",
                        new BigDecimal("200")
                ));

        assertEquals(currencyDto, amountOfCurrency);
    }
}