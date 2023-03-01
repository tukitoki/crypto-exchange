package ru.vsu.cs.raspopov.cryptoexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AmountOfUserCurrencyId implements Serializable {

    private UUID user;

    private Integer currency;
}
