package ru.vsu.cs.raspopov.cryptoexchange.dto;

import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {

    private interface Email {

        @NotBlank(message = "Enter your email")
        @Size(max = 200, message = "email length must be <= 200 characters")
        @javax.validation.constraints.Email(message = "Not valid email", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
        String getEmail();
    }

    private interface Username {
        @NotBlank(message = "Enter your username")
        @Size(max = 50, message = "username length must be <= 50 characters")
        String getUsername();
    }

    public enum Request {;

        @Value
        public static class UserRegistration implements UserDto.Email, Username {
            String email;
            String username;
        }

        @Data
        @Value
        public static class UserSecretKey implements Fields.SecretKey {
            String secretKey;
        }
    }

    public enum Response {;

        @Value
        public static class UserSecretKey implements Fields.SecretKey {
            String secretKey;
        }
    }
}
