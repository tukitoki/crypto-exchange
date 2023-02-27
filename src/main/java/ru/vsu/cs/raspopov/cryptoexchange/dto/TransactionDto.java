package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Transaction;
import ru.vsu.cs.raspopov.cryptoexchange.entity.TransactionType;

import java.sql.Timestamp;

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
        Timestamp getDate();
    }
    private interface DateFrom {

        @JsonProperty("date_from")
        Timestamp getDateFrom();
    }
    private interface DateTo {
        @JsonProperty("date_to")
        Timestamp getDateTo();
    }
    private interface TransactionCount {
        @JsonProperty("transaction_count")
        Integer getCount();
    }

    public static class Request {
        @Value
        public static class TransactionFromTo implements Fields.SecretKey, DateFrom, DateTo {
            String secretKey;
            Timestamp dateFrom;
            Timestamp dateTo;
        }
    }

    public static class Response {
        @Value
        public static class TransactionCounter implements TransactionCount {
            Integer count;
        }
    }
}
