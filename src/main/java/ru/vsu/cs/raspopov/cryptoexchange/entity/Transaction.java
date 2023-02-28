package ru.vsu.cs.raspopov.cryptoexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.TransactionType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    private Integer id;

    @NotBlank(message = "secret_key should not be blank")
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull(message = "Enter transaction dateTime")
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate date;

    public Transaction(String secretKey, TransactionType type, LocalDate date) {
        this.secretKey = secretKey;
        this.type = type;
        this.date = date;
    }
}
