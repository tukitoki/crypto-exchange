package ru.vsu.cs.raspopov.cryptoexchange.dto;

import lombok.Value;

public class AmountOfUserCurrencyDto {

    private interface AmountOfUserCurrencyId {
        AmountOfUserCurrencyId getAmountOfUserCurrencyId();
    }

    public enum Request {;

        @Value
        public static class CurrencyAmount implements Fields.Currency, Fields.Amount {
            String currency;
            Double amount;
        }
    }

    public enum Response {;

        @Value
        public static class CurrencyAmount implements Fields.Currency, Fields.Amount {
            String currency;
            Double amount;

        }
    }
}
