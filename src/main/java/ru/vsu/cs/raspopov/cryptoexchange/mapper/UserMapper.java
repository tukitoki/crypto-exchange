package ru.vsu.cs.raspopov.cryptoexchange.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final CurrencyRepository currencyRepository;

    public User toEntity(UserDto.Request.UserRegistration userDto) {
        User user = new User(userDto.getUsername(), userDto.getEmail());
        user.setRole(Role.USER);
        List<AmountOfUserCurrency> userCurrencyAmounts = new ArrayList<>();
        for (Currency currency : currencyRepository.findAll()) {
            userCurrencyAmounts.add(new AmountOfUserCurrency(user, currency, BigDecimal.ZERO));
        }
        user.setWallet(userCurrencyAmounts);
        return user;
    }
}
