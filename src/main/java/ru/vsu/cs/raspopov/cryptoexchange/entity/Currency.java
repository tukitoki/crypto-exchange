package ru.vsu.cs.raspopov.cryptoexchange.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private Integer id;

    @NotBlank(message = "currency name should not be blank")
    private String name;
    @OneToMany(mappedBy = "currency")
    private List<AmountOfUserCurrency> userCurrencyAmount;

    @OneToMany(mappedBy = "baseCurrency")
    private List<ExchangeRate> baseExchangeRates;

}
