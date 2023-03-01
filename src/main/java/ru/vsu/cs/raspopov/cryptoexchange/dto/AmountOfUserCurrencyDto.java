package ru.vsu.cs.raspopov.cryptoexchange.dto;

import lombok.Value;

import java.math.BigDecimal;

public class AmountOfUserCurrencyDto {

    public enum Request {;

        @Value
        public static class CurrencyAmount implements Fields.Currency, Fields.Amount {
            String currency;
            BigDecimal amount;
        }
    }

    public enum Response {;

        @Value
        public static class CurrencyAmount implements Fields.Currency, Fields.Amount {
            String currency;
            BigDecimal amount;
        }
    }
}
