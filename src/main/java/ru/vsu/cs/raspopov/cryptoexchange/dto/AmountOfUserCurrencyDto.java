package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

public class AmountOfUserCurrencyDto {

    private interface Currency {
        @NotBlank
        String getCurrency();
    }

    private interface AmountOfUserCurrencyId {
        AmountOfUserCurrencyId getAmountOfUserCurrencyId();
    }

    private interface Amount {
        @JsonProperty("count")
        double getAmount();
    }

    public static class Request {

        @Value
        public static class CurrencyAmount implements Currency, Amount {
            String currency;
            double amount;
        }
    }

    public static class Response {

        @Value
        public static class CurrencyAmount implements Currency, Amount {
            String currency;
            double amount;

        }
    }
}
