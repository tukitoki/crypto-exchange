package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDto {

    private interface Id {
        Integer getId();
    }
    private interface Type {
        @NotBlank
        TransactionType getType();
    }
    private interface Date {
        @NotBlank
        LocalDate getDate();
    }
    private interface DateFrom {

        @JsonProperty("date_from")
        @JsonFormat(pattern="dd.MM.yyyy")
        LocalDate getDateFrom();
    }
    private interface DateTo {
        @JsonProperty("date_to")
        @JsonFormat(pattern="dd.MM.yyyy")
        LocalDate getDateTo();
    }
    private interface TransactionCount {
        @JsonProperty("transaction_count")
        Integer getCount();
    }

    public enum Request {;
        @Value
        public static class TransactionFromTo implements Fields.SecretKey, DateFrom, DateTo {
            String secretKey;
            LocalDate dateFrom;
            LocalDate dateTo;
        }
    }

    public enum Response {;
        @Value
        public static class TransactionCounter implements TransactionCount {
            Integer count;
        }
        @Value
        public static class Transaction implements Fields.SecretKey, Date {
            String secretKey;
            LocalDate date;
        }
    }
}
