package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Value;

public class BalanceOperationDto {

    private interface SecretKey {
        @NotBlank
        @JsonProperty("secret_key")
        String getSecretKey();
    }

    private interface Currency {
        @NotBlank
        String getCurrency();
    }

    private interface Count {
        @Positive
        double getCount();
    }

    private interface MoneyWithdrawalPlatform {
        @NotBlank
        @JsonProperty("credit_card")
        @JsonAlias("wallet")
        String getMoneyWithdrawalPlatform();
    }

    private interface CurrencyFrom {

        @NotBlank
        @JsonProperty("currency_from")
        String getCurrencyFrom();
    }

    private interface CurrencyTo {

        @NotBlank
        @JsonProperty("currency_to")
        String getCurrencyTo();
    }

    private interface AmountTo {
        double getAmountTo();
    }

    private interface AmountFrom {
        double getAmountFrom();
    }

    public static class Request {
        @Value
        public static class ReplenishmentBalanceDto implements SecretKey, Currency, Count {
            String secretKey;
            String currency;
            double count;
        }

        @Value
        public static class WithdrawalBalanceDto implements SecretKey, Currency, Count, MoneyWithdrawalPlatform {
            String secretKey;
            String currency;
            double count;
            String moneyWithdrawalPlatform;
        }

        @Value
        public static class ExchangeCurrencyDto implements SecretKey, CurrencyFrom, CurrencyTo, Count {
            String secretKey;
            String currencyFrom;
            String currencyTo;
            @JsonProperty("amount")
            double count;
        }
    }

    public static class Response{

        @Value
        public static class ExchangeCurrencyDto implements CurrencyFrom, CurrencyTo, AmountFrom, AmountTo {
            String currencyFrom;
            String currencyTo;
            double amountFrom;
            double amountTo;
        }

    }

}
