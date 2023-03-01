package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.BalanceOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrencyId;
import ru.vsu.cs.raspopov.cryptoexchange.entity.ExchangeRate;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.TransactionType;
import ru.vsu.cs.raspopov.cryptoexchange.repository.AmountOfUserCurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.BalanceService;
import ru.vsu.cs.raspopov.cryptoexchange.service.TransactionService;
import ru.vsu.cs.raspopov.cryptoexchange.utils.ValidationUtil;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class BalanceServiceImpl implements BalanceService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AmountOfUserCurrencyRepository amountOfUserCurrencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final TransactionService transactionService;

    @Override
    public List<AmountOfUserCurrencyDto.Response.CurrencyAmount> getUserBalance(
            UserDto.Request.UserSecretKey userDto) {
        User user = ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(userDto.getSecretKey())));
        ValidationUtil.validUserRole(user, Role.USER);

        List<AmountOfUserCurrencyDto.Response.CurrencyAmount> userWallet = new ArrayList<>();
        user.getWallet().forEach(userCurrencyAmount -> {
            userWallet.add(new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                    userCurrencyAmount.getCurrency().getName(),
                    userCurrencyAmount.getAmount())
            );
        });

        log.info("User with secret_key: " + userDto.getSecretKey() + " successfully get balance");

        return userWallet;
    }

    @Transactional
    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount replenishmentBalance(
            BalanceOperationDto.Request.ReplenishmentBalance balanceDto) {
        User user = ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(balanceDto.getSecretKey())));
        ValidationUtil.validUserRole(user, Role.USER);

        var currency = ValidationUtil.validCurrency(currencyRepository.findByName(
                balanceDto.getCurrency()));

        var amountOfUserCurrency = amountOfUserCurrencyRepository.findById(
                new AmountOfUserCurrencyId(UUID.fromString(balanceDto.getSecretKey()),
                        currency.getId())).get();
        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount().add(balanceDto.getAmount()));
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);

        log.info("User with secret_key: " + balanceDto.getSecretKey() + "successfully replenishment "
                + balanceDto.getAmount() + balanceDto.getCurrency());

        transactionService.saveTransaction(TransactionType.REPLENISHMENT, balanceDto.getSecretKey());

        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                amountOfUserCurrency.getCurrency().getName(),
                amountOfUserCurrency.getAmount());
    }

    @Transactional
    @Override
    public AmountOfUserCurrencyDto.Response.CurrencyAmount withdrawalMoney(
            BalanceOperationDto.Request.WithdrawalBalance balanceDto) {
        User user = ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(balanceDto.getSecretKey())));
        ValidationUtil.validUserRole(user, Role.USER);

        var currency = ValidationUtil.validCurrency(currencyRepository.findByName(
                balanceDto.getCurrency()));

        var amountOfUserCurrency = amountOfUserCurrencyRepository.findById(
                new AmountOfUserCurrencyId(UUID.fromString(balanceDto.getSecretKey()),
                        currency.getId())).get();

        validWalletBalance(amountOfUserCurrency.getAmount(), balanceDto.getAmount());

        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount().subtract(balanceDto.getAmount()));
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);

        transactionService.saveTransaction(TransactionType.WITHDRAWAL, balanceDto.getSecretKey());

        log.info("User with secret_key: " + balanceDto.getSecretKey() + "successfully withdrawal "
                + balanceDto.getAmount() + balanceDto.getCurrency());

        return new AmountOfUserCurrencyDto.Response.CurrencyAmount(
                amountOfUserCurrency.getCurrency().getName(),
                amountOfUserCurrency.getAmount());
    }

    @Transactional
    @Override
    public BalanceOperationDto.Response.ExchangeCurrency exchangeCurrency(
            BalanceOperationDto.Request.ExchangeCurrency exchangeCurrency) {
        User user = ValidationUtil.validUser(userRepository.findById(
                UUID.fromString(exchangeCurrency.getSecretKey())));
        ValidationUtil.validUserRole(user, Role.USER);

        var baseCurrency = ValidationUtil.validCurrency(currencyRepository.findByName(
                exchangeCurrency.getCurrencyFrom()));
        var exchangeableCurrency = ValidationUtil.validCurrency(currencyRepository.findByName(
                exchangeCurrency.getCurrencyTo()));

        var baseUserCurrency = amountOfUserCurrencyRepository.findById(
                new AmountOfUserCurrencyId(UUID.fromString(exchangeCurrency.getSecretKey()),
                        baseCurrency.getId())).get();

        validWalletBalance(baseUserCurrency.getAmount(), exchangeCurrency.getAmount());

        var exchangeableUserCurrency = amountOfUserCurrencyRepository.findById(
                new AmountOfUserCurrencyId(UUID.fromString(exchangeCurrency.getSecretKey()),
                        exchangeableCurrency.getId())).get();

        BigDecimal exchangeAmount = exchangeCurrency(baseUserCurrency, exchangeableUserCurrency,
                exchangeCurrency.getAmount());

        log.info("User with secret_key: " + exchangeCurrency.getSecretKey() + "successfully exchange from "
                + exchangeCurrency.getCurrencyFrom() + " to " + exchangeCurrency.getCurrencyTo());

        transactionService.saveTransaction(TransactionType.EXCHANGE, exchangeCurrency.getSecretKey());

        return new BalanceOperationDto.Response.ExchangeCurrency(exchangeCurrency.getCurrencyFrom(),
                exchangeCurrency.getCurrencyTo(),
                exchangeCurrency.getAmount(),
                exchangeAmount);
    }

    private BigDecimal exchangeCurrency(AmountOfUserCurrency fromExchangeCurrency,
                                    AmountOfUserCurrency toExchangeCurrency,
                                    BigDecimal amountOfExchange) {
        ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyAndAnotherCurrency(
                fromExchangeCurrency.getCurrency(),
                toExchangeCurrency.getCurrency()).get();

        fromExchangeCurrency.setAmount(fromExchangeCurrency.getAmount().subtract(amountOfExchange));

        BigDecimal changedCurrency = exchangeRate.getExchangeRate().multiply(amountOfExchange);
        changedCurrency.add(toExchangeCurrency.getAmount());
        toExchangeCurrency.setAmount(changedCurrency);

        amountOfUserCurrencyRepository.save(fromExchangeCurrency);
        toExchangeCurrency = amountOfUserCurrencyRepository.save(toExchangeCurrency);
        return toExchangeCurrency.getAmount();
    }

    private void validWalletBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough balance on wallet");
        }
    }
}
