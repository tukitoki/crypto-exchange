package ru.vsu.cs.raspopov.cryptoexchange.service;

import ru.vsu.cs.raspopov.cryptoexchange.dto.ExchangeCurrencyDto;

import java.util.List;

public interface ExchangeService {

    List<ExchangeCurrencyDto.Response.CurrencyExchange> getExchangeRate(
            ExchangeCurrencyDto.Request.SecretKeyCurrency exchangeDto);

    List<ExchangeCurrencyDto.Response.CurrencyExchange> updateExchangeRates(
            ExchangeCurrencyDto.Request.ChangeExchangeRate exchangeDto);
}
