package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class ExchangeCurrencyDto {

    private interface ExchangeRate {
        @JsonProperty("exchange_rate")
        @Positive(message = "Exchange_rate should be >0")
        BigDecimal getExchangeRate();
    }

    private interface Currencies {
        @Size(min = 1, message = "Currencies count should be >=1")
        List<ExchangeCurrencyDto.Response.CurrencyExchange> getCurrencies();
    }

    public enum Request {;

        @Value
        public static class SecretKeyCurrency implements Fields.SecretKey, Fields.Currency {
            String secretKey;
            @JsonProperty("base_currency")
            String currency;
        }

        @Value
        public static class ChangeExchangeRate implements Fields.SecretKey, Fields.Currency, Currencies {
            String secretKey;
            @JsonProperty("base_currency")
            String currency;
            List<ExchangeCurrencyDto.Response.CurrencyExchange> currencies;
        }
    }

    public enum Response {;

        @Value
        public static class CurrencyExchange implements ExchangeRate, Fields.Currency {
            String currency;
            BigDecimal exchangeRate;
        }
    }
}
