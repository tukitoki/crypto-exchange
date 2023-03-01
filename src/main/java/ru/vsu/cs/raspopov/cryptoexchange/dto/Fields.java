package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public enum Fields {;

    protected interface Currency {
        @NotBlank(message = "Type currency name")
        String getCurrency();
    }
    protected interface SecretKey {
        @NotBlank(message = "Type user secret_key")
        @JsonProperty("secret_key")
        String getSecretKey();
    }
    protected interface Amount {
        @Positive(message = "Amount should be >0")
        @NotNull(message = "Amount should not be empty")
        BigDecimal getAmount();
    }
}
