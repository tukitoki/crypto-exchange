package ru.vsu.cs.raspopov.cryptoexchange.utils;

import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ValidationUtil {

    public static void validUserRole(User user, Role expectedRole) {
        if (!user.getRole().equals(expectedRole)) {
            try {
                throw new AccessDeniedException("Access only for " + expectedRole.toString());
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static Currency validCurrency(Optional<Currency> optionalCurrency) {
        if (optionalCurrency.isEmpty()) {
            throw new NoSuchElementException("Wrong currency name");
        }
        return optionalCurrency.get();
    }
    public static User validUser(Optional<User> optionalUser) {
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("Wrong user secret_key");
        }

        return optionalUser.get();
    }
}
