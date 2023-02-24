package ru.vsu.cs.raspopov.cryptoexchange.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exchange_user")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id", columnDefinition = "VARCHAR(40)",
            insertable = false, updatable = false, nullable = false)
    private String secretKey;

    @NotBlank(message = "Enter your username")
    @Size(max = 50, message = "username length must be <= 50 characters")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "enter ur email")
    @Size(max = 200, message = "email length must be <= 200 characters")
    @Email(message = "Not valid email", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @Column(unique = true)

    private String email;
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AmountOfUserCurrency> wallet;
}
