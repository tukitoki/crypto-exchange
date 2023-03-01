package ru.vsu.cs.raspopov.cryptoexchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.ExchangeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ExchangeControllerTest {

    @Mock
    private ExchangeService exchangeService;
    @InjectMocks
    private ExchangeController exchangeController;

    @Test
    void getExchangeRate_ReturnsValidResponseEntity() {
        var exchangeRates = List.of(new CurrencyDto.Response.CurrencyExchange("RUB", 0.5),
                new CurrencyDto.Response.CurrencyExchange("BTC", 0.0005));

        var currencyDto = new CurrencyDto.Request.SecretKeyCurrency("fff", "ton");
        Mockito.doReturn(exchangeRates).when(this.exchangeService).getExchangeRate(currencyDto);
        var responseEntity = this.exchangeController.getExchangeRate(currencyDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(exchangeRates, responseEntity.getBody());
    }

    @Test
    void changeExchangeRates_ReturnsValidResponseEntity() {
        var changedExchangeRates = List.of(new CurrencyDto.Response.CurrencyExchange("RUB", 0.1),
                new CurrencyDto.Response.CurrencyExchange("BTC", 0.05));

        var currencyDto = new CurrencyDto.Request.ChangeExchangeRate("fff", "TON", changedExchangeRates);
        Mockito.doReturn(changedExchangeRates).when(this.exchangeService).updateExchangeRates(currencyDto);
        var responseEntity = this.exchangeController.changeExchangeRates(currencyDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(changedExchangeRates, responseEntity.getBody());
    }
}