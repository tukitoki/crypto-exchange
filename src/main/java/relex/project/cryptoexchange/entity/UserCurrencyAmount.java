package relex.project.cryptoexchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserCurrencyAmountId.class)
@Entity
@Table(name = "user_currency_amount")
public class UserCurrencyAmount {

    @Id
    @JoinColumn(name = "user_secret_key")
    @ManyToOne
    private User user;

    @Id
    @JoinColumn(name = "currency_id")
    @ManyToOne
    private Currency currency;
    private double amount;

}
