package ru.vsu.cs.raspopov.cryptoexchange.service;

import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDto.Response.CurrencyExchangeDto> getExchangeRate(
            CurrencyDto.Request.SecretKeyCurrencyDto currencyDto);

    List<CurrencyDto.Response.CurrencyExchangeDto> updateExchangeRates(
            CurrencyDto.Request.ChangeExchangeRateDto currencyDto);

    AmountOfUserCurrencyDto.Response.CurrencyAmount getTotalAmountOfCurrency(
            CurrencyDto.Request.SecretKeyCurrencyDto currencyDto);


}
