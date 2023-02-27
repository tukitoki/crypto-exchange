package ru.vsu.cs.raspopov.cryptoexchange.service;

import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDto.Response.CurrencyExchange> getExchangeRate(
            CurrencyDto.Request.SecretKeyCurrency currencyDto);

    List<CurrencyDto.Response.CurrencyExchange> updateExchangeRates(
            CurrencyDto.Request.ChangeExchangeRate currencyDto);

    AmountOfUserCurrencyDto.Response.CurrencyAmount getTotalAmountOfCurrency(
            CurrencyDto.Request.SecretKeyCurrency currencyDto);


}
