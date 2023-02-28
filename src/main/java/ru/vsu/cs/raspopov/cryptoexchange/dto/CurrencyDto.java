package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public class CurrencyDto {

    private interface Id {
        Integer getId();
    }
    private interface ExchangeRate {
        @JsonProperty("exchange_rate")
        @Positive(message = "Exchange_rate should be >0")
        Double getExchangeRate();
    }
    private interface Currencies {
        @Size(min = 1, message = "Currencies count should be >=1")
        List<Response.CurrencyExchange> getCurrencies();
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
            List<Response.CurrencyExchange> currencies;
        }
    }

    public enum Response {;
        @Value
        public static class CurrencyExchange implements ExchangeRate, Fields.Currency {
            String currency;
            Double exchangeRate;
        }
        @Value
        public static class TotalCurrencyAmount implements Fields.Currency, Fields.Amount {
            String currency;
            Double amount;
        }
    }

}
