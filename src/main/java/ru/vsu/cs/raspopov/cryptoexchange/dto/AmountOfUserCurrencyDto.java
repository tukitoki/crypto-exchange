package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Value;

public class AmountOfUserCurrencyDto {

    private interface AmountOfUserCurrencyId {
        AmountOfUserCurrencyId getAmountOfUserCurrencyId();
    }

    public static class Request {

        @Value
        public static class CurrencyAmount implements Fields.Currency, Fields.Amount {
            String currency;
            Double amount;
        }
    }

    public static class Response {

        @Value
        public static class CurrencyAmount implements Fields.Currency, Fields.Amount {
            String currency;
            Double amount;

        }
    }
}
