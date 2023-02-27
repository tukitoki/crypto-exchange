package ru.vsu.cs.raspopov.cryptoexchange.entity;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AmountOfUserCurrencyId implements Serializable {

    private String user;

    private Integer currency;
}
