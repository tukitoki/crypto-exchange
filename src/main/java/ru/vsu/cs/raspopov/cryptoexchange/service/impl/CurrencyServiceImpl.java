package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.repository.AmountOfUserCurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.CurrencyService;
import ru.vsu.cs.raspopov.cryptoexchange.utils.ValidationUtil;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final AmountOfUserCurrencyRepository amountOfUserCurrencyRepository;

    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount getTotalAmountOfCurrency(
            CurrencyDto.Request.SecretKeyCurrency currencyDto) {
        User user = ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(currencyDto.getSecretKey()))
        );
        ValidationUtil.validUserRole(user, Role.ADMIN);

        Currency baseCurrency = ValidationUtil.validCurrency(currencyRepository.findByName(
                currencyDto.getCurrency()));

        double totalCurrencyAmount = 0;
        for (AmountOfUserCurrency amountOfUserCurrency : amountOfUserCurrencyRepository
                .findAllByCurrency(baseCurrency)) {
            totalCurrencyAmount += amountOfUserCurrency.getAmount();
        }

        log.info("ADMIN successfully get total amount of currency");

        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(currencyDto.getCurrency(),
                totalCurrencyAmount);
    }
}
