package ru.vsu.cs.raspopov.cryptoexchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


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
    private int id;
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private Timestamp date;

    public Transaction(String secretKey, TransactionType type, Timestamp date) {
        this.secretKey = secretKey;
        this.type = type;
        this.date = date;
    }
}
