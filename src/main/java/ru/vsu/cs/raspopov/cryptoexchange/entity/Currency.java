package ru.vsu.cs.raspopov.cryptoexchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private int id;
    private String name;
    private double exchangeRateToTheRuble;
    @OneToMany(mappedBy = "currency")
    private List<AmountOfUserCurrency> userCurrencyAmount;
}
