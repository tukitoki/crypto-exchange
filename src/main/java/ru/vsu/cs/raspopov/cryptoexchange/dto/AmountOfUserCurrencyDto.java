package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrencyId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountOfUserCurrencyDto {

    private String currency;

    @JsonIgnore
    private AmountOfUserCurrencyId amountOfUserCurrencyId;

    @JsonProperty("count")
    private double amount;
}
