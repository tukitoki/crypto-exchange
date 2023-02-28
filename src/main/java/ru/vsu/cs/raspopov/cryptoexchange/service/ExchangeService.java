package ru.vsu.cs.raspopov.cryptoexchange.service;

import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;

import java.util.List;

public interface ExchangeService {

    List<CurrencyDto.Response.CurrencyExchange> getExchangeRate(CurrencyDto.Request.SecretKeyCurrency currencyDto);
    List<CurrencyDto.Response.CurrencyExchange> updateExchangeRates(CurrencyDto.Request.ChangeExchangeRate currencyDto);
}
