package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.BalanceOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.*;
import ru.vsu.cs.raspopov.cryptoexchange.repository.*;
import ru.vsu.cs.raspopov.cryptoexchange.service.BalanceService;

import java.sql.Timestamp;
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
    private final TransactionRepository transactionRepository;

    @Override
    public List<AmountOfUserCurrencyDto.Response.CurrencyAmount> getUserBalance(UserDto userDto) {
        User user = userRepository.findById(userDto.getSecretKey())
                .orElseThrow(() -> new NoSuchElementException("Wrong user secret_key"));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Access only for USER");
        }

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
            BalanceOperationDto.Request.ReplenishmentBalance balanceDto) {
        User user = userRepository.findById(balanceDto.getSecretKey())
                .orElseThrow(() -> new NoSuchElementException("Wrong user secret_key"));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Access only for USER");
        }

        if (currencyRepository.findByName(balanceDto.getCurrency()).isEmpty()) {
            throw new IllegalArgumentException("Wrong currency value");
        }

        Currency currency = currencyRepository.findByName(balanceDto.getCurrency()).get();

        AmountOfUserCurrency amountOfUserCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(balanceDto.getSecretKey(),
                        currency.getId()))
                .get();
        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount() + balanceDto.getCount());
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);

        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                amountOfUserCurrency.getCurrency().getName(),
                amountOfUserCurrency.getAmount());
    }

    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount withdrawalMoney(
            BalanceOperationDto.Request.WithdrawalBalance balanceDto) {
        User user = userRepository.findById(balanceDto.getSecretKey())
                .orElseThrow(() -> new NoSuchElementException("Wrong user secret_key"));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Access only for USER");
        }

        if (currencyRepository.findByName(balanceDto.getCurrency()).isEmpty()) {
            throw new IllegalArgumentException("Wrong currency value");
        }

        Currency currency = currencyRepository.findByName(balanceDto.getCurrency()).get();

        AmountOfUserCurrency amountOfUserCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(balanceDto.getSecretKey(), currency.getId()))
                .get();

        if (amountOfUserCurrency.getAmount() < balanceDto.getCount()) {
            throw new RuntimeException("Not enough balance on wallet");
        }

        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount() - balanceDto.getCount());
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);

        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                amountOfUserCurrency.getCurrency().getName(),
                amountOfUserCurrency.getAmount());
    }

    @Override
    public BalanceOperationDto.Response.ExchangeCurrency exchangeCurrency(
            BalanceOperationDto.Request.ExchangeCurrency exchangeCurrency) {
        User user = userRepository.findById(exchangeCurrency.getSecretKey())
                .orElseThrow(() -> new NoSuchElementException("Wrong user secret_key"));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Access only for USER");
        }

        if (currencyRepository.findByName(exchangeCurrency.getCurrencyTo()).isEmpty()) {
            throw new NoSuchElementException("Invalid currency_to");
        }
        if (currencyRepository.findByName(exchangeCurrency.getCurrencyFrom()).isEmpty()) {
            throw new NoSuchElementException("Invalid currency_from");
        }

        Currency baseCurrency = currencyRepository.findByName(exchangeCurrency.getCurrencyFrom()).get();

        AmountOfUserCurrency fromExchangeCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(exchangeCurrency.getSecretKey(),
                        baseCurrency.getId()))
                .get();
        if (fromExchangeCurrency.getAmount() < exchangeCurrency.getCount()) {
            throw new RuntimeException("Not enough balance on wallet");
        }

        Currency exchangeToCurrency = currencyRepository.findByName(exchangeCurrency.getCurrencyTo()).get();

        AmountOfUserCurrency toExchangeCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(exchangeCurrency.getSecretKey(),
                        exchangeToCurrency.getId()))
                .get();

        AmountOfUserCurrency updatedToExchangeCurrency = exchangeCurrency(fromExchangeCurrency,
                toExchangeCurrency,
                exchangeCurrency.getCount());

        saveTransaction(TransactionType.EXCHANGE, exchangeCurrency.getSecretKey());

        return new BalanceOperationDto.Response.ExchangeCurrency(exchangeCurrency.getCurrencyFrom(),
                exchangeCurrency.getCurrencyTo(),
                exchangeCurrency.getCount(),
                updatedToExchangeCurrency.getAmount());
    }

    private AmountOfUserCurrency exchangeCurrency(AmountOfUserCurrency fromExchangeCurrency,
                                                  AmountOfUserCurrency toExchangeCurrency,
                                                  Double amountOfExchange) {
        ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(
                fromExchangeCurrency.getCurrency(),
                toExchangeCurrency.getCurrency()).get();

        fromExchangeCurrency.setAmount(fromExchangeCurrency.getAmount() - amountOfExchange);
        toExchangeCurrency.setAmount(exchangeRate.getExchangeRate()
                * amountOfExchange
                + toExchangeCurrency.getAmount());

        amountOfUserCurrencyRepository.save(fromExchangeCurrency);
        return amountOfUserCurrencyRepository.save(toExchangeCurrency);
    }

    private void saveTransaction(TransactionType type, String secretKey) {
        transactionRepository.save(new Transaction(secretKey, type, new Timestamp(System.currentTimeMillis())));
    }
}
