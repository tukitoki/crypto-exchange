package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public enum Fields {;

    protected interface Currency {
        @NotBlank(message = "Type currency name")
        @JsonProperty("currency")
        String getCurrency();
    }
    protected interface SecretKey {
        @NotBlank(message = "Type user secret_key")
        @JsonProperty("secret_key")
        String getSecretKey();
    }
    protected interface Amount {
        @Positive(message = "Amount should be >0")
        Double getAmount();
    }
}
