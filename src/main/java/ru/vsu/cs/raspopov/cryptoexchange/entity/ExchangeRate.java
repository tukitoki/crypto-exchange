package ru.vsu.cs.raspopov.cryptoexchange.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_rate_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency baseCurrency;

    @OneToOne
    private Currency anotherCurrency;

    @Column(name = "exchange_rate")
    private double exchangeRate;
}
