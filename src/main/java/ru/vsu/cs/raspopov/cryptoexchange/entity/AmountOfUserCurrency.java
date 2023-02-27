package ru.vsu.cs.raspopov.cryptoexchange.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
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

    @Min(value = 0, message = "amount should be >=0")
    private Double amount;

}
