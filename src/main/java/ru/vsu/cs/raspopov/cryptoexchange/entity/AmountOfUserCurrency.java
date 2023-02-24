package ru.vsu.cs.raspopov.cryptoexchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AmountOfUserCurrencyId.class)
@Entity
@Table(name = "user_currency_amount")
public class AmountOfUserCurrency {

    @Id
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Id
    @JoinColumn(name = "currency_id")
    @ManyToOne
    private Currency currency;

    private double amount;

}
