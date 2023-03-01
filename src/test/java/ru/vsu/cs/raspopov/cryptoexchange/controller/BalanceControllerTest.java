package ru.vsu.cs.raspopov.cryptoexchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.BalanceOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.BalanceService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;
    @InjectMocks
    private BalanceController balanceController;

    @Test
    void walletBalance_ReturnsValidResponseEntity() {
        var userWallet = List.of(new AmountOfUserCurrencyDto.Response.CurrencyAmount("RUB",
                        new BigDecimal("0")),
                new AmountOfUserCurrencyDto.Response.CurrencyAmount("TON", new BigDecimal("0")),
                new AmountOfUserCurrencyDto.Response.CurrencyAmount("BTC", new BigDecimal("0.1")));
        var userDto = new UserDto.Request.UserSecretKey();
        userDto.setSecretKey("fff");
        doReturn(userWallet).when(this.balanceService).getUserBalance(userDto);

        var responseEntity = this.balanceController.walletBalance(userDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userWallet, responseEntity.getBody());
    }

    @Test
    void replenishmentBalance_ReturnValidResponseEntity() {
        var currency = new AmountOfUserCurrencyDto.Response.CurrencyAmount("RUB",
                new BigDecimal("2000"));
        var replBalance = new BalanceOperationDto.Request
                .ReplenishmentBalance("fff", "RUB", new BigDecimal("2000"));

        doReturn(currency).when(this.balanceService).replenishmentBalance(replBalance);

        var responseEntity = this.balanceController.replenishmentBalance(replBalance);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(currency, responseEntity.getBody());
    }

    @Test
    void withdrawalMoney_ReturnValidResponseEntity() {
        var currency = new AmountOfUserCurrencyDto.Response.CurrencyAmount("TON",
                new BigDecimal("2000"));
        var withdrBalance = new BalanceOperationDto.Request
                .WithdrawalBalance("fff", "TON",
                new BigDecimal("200"),
                "AAA");

        doReturn(currency).when(this.balanceService).withdrawalMoney(withdrBalance);

        var responseEntity = this.balanceController.withdrawalMoney(withdrBalance);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(currency, responseEntity.getBody());
    }

    @Test
    void exchangeCurrency_ReturnValidResponseEntity() {
        var currency = new BalanceOperationDto.Request
                .ExchangeCurrency("fff", "RUB", "TON", new BigDecimal("2"));
        var exchangeBalance = new BalanceOperationDto.Response
                .ExchangeCurrency("RUB", "TON",
                new BigDecimal("0"),
                new BigDecimal("2"));

        doReturn(exchangeBalance).when(this.balanceService).exchangeCurrency(currency);

        var responseEntity = this.balanceController.exchangeCurrency(currency);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(exchangeBalance, responseEntity.getBody());
    }
}