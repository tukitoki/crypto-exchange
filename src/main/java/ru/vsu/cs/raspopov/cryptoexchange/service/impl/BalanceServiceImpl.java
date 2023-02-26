package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.BalanceOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.*;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.AmountOfUserCurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.BalanceService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AmountOfUserCurrencyRepository amountOfUserCurrencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<AmountOfUserCurrencyDto.Response.CurrencyAmount> getUserBalance(UserDto userDto) {
        if (userRepository.findById(userDto.getSecretKey()).isEmpty()) {
            throw new IllegalArgumentException("wrong user secret_key");
        }
        User user = userRepository.findById(userDto.getSecretKey()).get();
        List<AmountOfUserCurrencyDto.Response.CurrencyAmount> userWallet = new ArrayList<>();
        user.getWallet().forEach(userCurrencyAmount -> {
            userWallet.add(new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                    userCurrencyAmount.getCurrency().getName(),
                    userCurrencyAmount.getAmount()));
        });
        return userWallet;
    }

    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount replenishmentBalance(
            BalanceOperationDto.Request.ReplenishmentBalanceDto balanceDto) {
        if (userRepository.findById(balanceDto.getSecretKey()).isEmpty()) {
            throw new IllegalArgumentException("wrong user secret_key");
        }
        if (currencyRepository.findByName(balanceDto.getCurrency()).isEmpty()) {
            throw new IllegalArgumentException("wrong currency value");
        }
        Currency currency = currencyRepository.findByName(balanceDto.getCurrency()).get();
        AmountOfUserCurrency amountOfUserCurrency = amountOfUserCurrencyRepository.findById(
                new AmountOfUserCurrencyId(balanceDto.getSecretKey(),
                        currency.getId())).get();
        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount() + balanceDto.getCount());
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);
        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                amountOfUserCurrency.getCurrency().getName(),
                amountOfUserCurrency.getAmount());
    }

    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount withdrawalMoney(
            BalanceOperationDto.Request.WithdrawalBalanceDto balanceDto) {
        if (userRepository.findById(balanceDto.getSecretKey()).isEmpty()) {
            throw new IllegalArgumentException("wrong user secret_key");
        }
        if (currencyRepository.findByName(balanceDto.getCurrency()).isEmpty()) {
            throw new IllegalArgumentException("wrong currency value");
        }
        Currency currency = currencyRepository.findByName(balanceDto.getCurrency()).get();
        AmountOfUserCurrency amountOfUserCurrency = amountOfUserCurrencyRepository.findById(
                new AmountOfUserCurrencyId(balanceDto.getSecretKey(),
                        currency.getId())).get();
        if (amountOfUserCurrency.getAmount() < balanceDto.getCount()) {
            throw new RuntimeException("not enough balance on wallet");
        }
        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount() - balanceDto.getCount());
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);
        //        amountOfUserCurrencyDto.setCurrency(balanceDto.getCurrency());
        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                amountOfUserCurrency.getCurrency().getName(),
                amountOfUserCurrency.getAmount());
    }

    @Override
    public BalanceOperationDto.Response.ExchangeCurrencyDto exchangeCurrency(
            BalanceOperationDto.Request.ExchangeCurrencyDto exchangeCurrencyDto) {
        if (userRepository.findById(exchangeCurrencyDto.getSecretKey()).isEmpty()) {
            throw new NoSuchElementException("invalid secret_key");
        }
        if (currencyRepository.findByName(exchangeCurrencyDto.getCurrencyTo()).isEmpty()) {
            throw new NoSuchElementException("invalid currency_to");
        }
        if (currencyRepository.findByName(exchangeCurrencyDto.getCurrencyFrom()).isEmpty()) {
            throw new NoSuchElementException("invalid currency_from");
        }
        Currency baseCurrency = currencyRepository.findByName(exchangeCurrencyDto.getCurrencyFrom()).get();
        AmountOfUserCurrency amountOfUserCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(exchangeCurrencyDto.getSecretKey(),
                        baseCurrency.getId())).get();
        if (amountOfUserCurrency.getAmount() < exchangeCurrencyDto.getCount()) {
            throw new RuntimeException("not enough balance on wallet");
        }
        Currency exchangeToCurrency = currencyRepository.findByName(exchangeCurrencyDto.getCurrencyTo()).get();
        AmountOfUserCurrency amountOfUserToExchangeCurrency = amountOfUserCurrencyRepository.findById(
                new AmountOfUserCurrencyId(exchangeCurrencyDto.getSecretKey(),
                        exchangeToCurrency.getId())).get();
        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount() - exchangeCurrencyDto.getCount());
        ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(baseCurrency,
                exchangeToCurrency).get();
        amountOfUserToExchangeCurrency.setAmount(exchangeRate.getExchangeRate()
                * exchangeCurrencyDto.getCount()
                + amountOfUserToExchangeCurrency.getAmount());
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);
        amountOfUserCurrencyRepository.save(amountOfUserToExchangeCurrency);
        return new BalanceOperationDto.Response.ExchangeCurrencyDto(exchangeCurrencyDto.getCurrencyFrom(),
                exchangeCurrencyDto.getCurrencyTo(),
                exchangeCurrencyDto.getCount(),
                amountOfUserToExchangeCurrency.getAmount());
    }
}
