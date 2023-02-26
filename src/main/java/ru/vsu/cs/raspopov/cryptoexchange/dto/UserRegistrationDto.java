package ru.vsu.cs.raspopov.cryptoexchange.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "enter ur email")
    @Size(max = 200, message = "email length must be <= 200 characters")
    @Email(message = "Not valid email", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    @NotBlank(message = "Enter your username")
    @Size(max = 50, message = "username length must be <= 50 characters")
    private String username;

}
