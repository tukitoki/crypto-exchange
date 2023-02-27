package ru.vsu.cs.raspopov.cryptoexchange.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


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
