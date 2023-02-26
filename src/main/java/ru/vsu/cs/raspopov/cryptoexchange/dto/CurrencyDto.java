package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class CurrencyDto {

    private interface SecretKey {
        @JsonProperty("secret_key")
        String getSecretKey();
    }

    private interface Id {
        int getId();
    }

    private interface Name {
        @JsonProperty("currency")
        String getName();
    }

    private interface ExchangeRate {
        @JsonProperty("exchange_rate")
        double getExchangeRate();
    }
    private interface CurrencyAmount {
        @JsonProperty("amount")
        double getAmount();
    }
    private interface Currencies {
        List<CurrencyDto.Response.CurrencyExchangeDto> getCurrencies();
    }

    public static class Request {
        @Value
        public static class SecretKeyCurrencyDto implements SecretKey, Name {
            String secretKey;
            String name;
        }
        @Value
        public static class ChangeExchangeRateDto implements SecretKey, Name, Currencies {
            String secretKey;
            @JsonProperty("base_currency")
            String name;
            List<CurrencyDto.Response.CurrencyExchangeDto> currencies;
        }
    }

    public static class Response {
        @Value
        public static class CurrencyExchangeDto implements ExchangeRate, Name {
            String name;
            double exchangeRate;
        }
        @Value
        public static class TotalCurrencyAmount implements Name, CurrencyAmount {
            String name;
            double amount;
        }
    }

}
