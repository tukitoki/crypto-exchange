package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class BalanceOperationDto {

    private interface MoneyWithdrawalPlatform {
        @NotBlank(message = "Type credit_card or wallet platform")
        @JsonProperty("credit_card")
        @JsonAlias("wallet")
        String getMoneyWithdrawalPlatform();
    }
    private interface CurrencyFrom {
        @NotBlank(message = "Type currency_from")
        @JsonProperty("currency_from")
        String getCurrencyFrom();
    }
    private interface CurrencyTo {
        @NotBlank(message = "Type currency_to")
        @JsonProperty("currency_to")
        String getCurrencyTo();
    }
    private interface AmountTo {
        @Positive(message = "Amount_to should be >0")
        @JsonProperty("amount_to")
        Double getAmountTo();
    }
    private interface AmountFrom {
        @Positive(message = "Amount_from should be >0")
        @JsonProperty("amount_from")
        Double getAmountFrom();
    }

    public enum Request {;
        @Value
        public static class ReplenishmentBalance implements Fields.SecretKey, Fields.Currency, Fields.Amount {
            String secretKey;
            String currency;
            Double amount;
        }
        @Value
        public static class WithdrawalBalance implements Fields.SecretKey, Fields.Currency, Fields.Amount,
                MoneyWithdrawalPlatform {
            String secretKey;
            String currency;
            Double amount;
            String moneyWithdrawalPlatform;
        }
        @Value
        public static class ExchangeCurrency implements Fields.SecretKey, CurrencyFrom, CurrencyTo, Fields.Amount {
            String secretKey;
            String currencyFrom;
            String currencyTo;
            Double amount;
        }
    }

    public enum Response {;
        @Value
        public static class ExchangeCurrency implements CurrencyFrom, CurrencyTo, AmountFrom, AmountTo {
            String currencyFrom;
            String currencyTo;
            Double amountFrom;
            Double amountTo;
        }

    }

}
