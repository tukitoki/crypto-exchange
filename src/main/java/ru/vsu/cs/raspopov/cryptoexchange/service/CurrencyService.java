package ru.vsu.cs.raspopov.cryptoexchange.service;

import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;

public interface CurrencyService {

    AmountOfUserCurrencyDto.Response.CurrencyAmount getTotalAmountOfCurrency(
            CurrencyDto.Request.SecretKeyCurrency currencyDto);


}
