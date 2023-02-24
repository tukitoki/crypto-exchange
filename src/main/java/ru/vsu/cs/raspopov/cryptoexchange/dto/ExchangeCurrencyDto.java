package ru.vsu.cs.raspopov.cryptoexchange.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ExchangeCurrencyDto {

    @NotBlank
    @JsonProperty("secret_key")
    private String secretKey;
    @NotBlank
    @JsonProperty("currency_from")
    private String currencyFrom;
    @NotBlank
    @JsonProperty("currency_to")
    private String currencyTo;
    @NotNull
    private double amount;

    @JsonProperty("amount_from")
    private double amountFrom;

    @JsonProperty("amount_to")
    private double amountTo;
}
