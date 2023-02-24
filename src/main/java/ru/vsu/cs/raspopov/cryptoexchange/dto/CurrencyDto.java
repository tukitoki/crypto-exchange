package ru.vsu.cs.raspopov.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class CurrencyDto {

    @JsonIgnore
    private int id;

    @JsonProperty("currency")
    private String name;

    @JsonProperty("exchange_rate")
    private double exchangeRateToTheRuble;

    public CurrencyDto(String name, double exchangeRateToTheRuble) {
        this.name = name;
        this.exchangeRateToTheRuble = exchangeRateToTheRuble;
    }
}
