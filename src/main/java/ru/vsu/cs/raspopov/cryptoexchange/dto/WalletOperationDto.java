package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletOperationDto {

    @JsonProperty("secret_key")
    @NotBlank
    private String secretKey;

    @NotBlank
    private String currency;

    @NotNull
    private int count;
    @JsonProperty("credit_card")
    @JsonAlias("wallet")
    private String moneyWithdrawalPlatform;

}
