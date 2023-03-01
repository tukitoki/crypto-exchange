package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

public class CurrencyDto {

    private interface Id {
        Integer getId();
    }

    public enum Request {;

        @Value
        public static class SecretKeyCurrency implements Fields.SecretKey, Fields.Currency {
            String secretKey;
            @JsonProperty("base_currency")
            String currency;
        }
    }

    public enum Response {;

        @Value
        public static class TotalCurrencyAmount implements Fields.Currency, Fields.Amount {
            String currency;
            Double amount;
        }
    }

}
