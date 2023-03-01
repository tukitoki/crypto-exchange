package ru.vsu.cs.raspopov.cryptoexchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.CurrencyService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;
    @InjectMocks
    private CurrencyController currencyController;

    @Test
    void getTotalAmountOfCurrency_ReturnsValidResponseEntity() {
        var amount = new AmountOfUserCurrencyDto.Response.CurrencyAmount("RUB",
                new BigDecimal("2000"));

        var currencyDto = new CurrencyDto.Request.SecretKeyCurrency("fff", "RUB");
        doReturn(amount).when(this.currencyService).getTotalAmountOfCurrency(currencyDto);
        var responseEntity = this.currencyController.getTotalAmountOfCurrency(currencyDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(amount, responseEntity.getBody());
    }
}