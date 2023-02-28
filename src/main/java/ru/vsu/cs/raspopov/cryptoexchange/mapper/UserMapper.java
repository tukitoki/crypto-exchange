package ru.vsu.cs.raspopov.cryptoexchange.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserRegistrationDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper = new ModelMapper();
    private final CurrencyRepository currencyRepository;
    public User toEntity(UserRegistrationDto userDto) {
        User user = mapper.map(userDto, User.class);
        user.setRole(Role.USER);
        List<AmountOfUserCurrency> userCurrencyAmounts = new ArrayList<>();
        for (Currency currency : currencyRepository.findAll()) {
            userCurrencyAmounts.add(new AmountOfUserCurrency(user, currency, 0.0));
        }
        user.setWallet(userCurrencyAmounts);
        return user;
    }
}
