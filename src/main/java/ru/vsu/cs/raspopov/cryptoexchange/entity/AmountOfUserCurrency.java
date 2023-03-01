package ru.vsu.cs.raspopov.cryptoexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

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
    @Column(columnDefinition = "numeric")
    private BigDecimal amount;

}
