package ru.vsu.cs.raspopov.cryptoexchange.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.ExchangeCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.WalletOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.AmountOfUserCurrencyId;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.AmountOfUserCurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class WalletService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AmountOfUserCurrencyRepository amountOfUserCurrencyRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<AmountOfUserCurrencyDto> getUserBalance(UserDto userDto) {
        if (userRepository.findById(userDto.getSecretKey()).isEmpty()) {
            throw new IllegalArgumentException("wrong user secret_key");
        }
        User user = userRepository.findById(userDto.getSecretKey()).get();
        List<AmountOfUserCurrencyDto> userWallet = new ArrayList<>();
        user.getWallet().forEach(userCurrencyAmount -> {
            userWallet.add(new AmountOfUserCurrencyDto(userCurrencyAmount.getCurrency().getName(),
                    new AmountOfUserCurrencyId(userCurrencyAmount.getUser().getSecretKey(),
                            userCurrencyAmount.getCurrency().getId()),
                    userCurrencyAmount.getAmount()));
        });
        return userWallet;
    }

    public AmountOfUserCurrencyDto replenishmentBalance(WalletOperationDto userDto) {
        if (userRepository.findById(userDto.getSecretKey()).isEmpty()) {
            throw new IllegalArgumentException("wrong user secret_key");
        }
        if (currencyRepository.findByName(userDto.getCurrency()).isEmpty()) {
            throw new IllegalArgumentException("wrong currency value");
        }
        Currency currency = currencyRepository.findByName(userDto.getCurrency()).get();
        AmountOfUserCurrency amountOfUserCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(userDto.getSecretKey(),
                        currency.getId())).get();
        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount() + userDto.getCount());
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);
        AmountOfUserCurrencyDto amountOfUserCurrencyDto = modelMapper.map(amountOfUserCurrency,
                AmountOfUserCurrencyDto.class);
        amountOfUserCurrencyDto.setCurrency(userDto.getCurrency());
        return amountOfUserCurrencyDto;
    }

    public AmountOfUserCurrencyDto withdrawalMoney(WalletOperationDto userDto) {
        if (userRepository.findById(userDto.getSecretKey()).isEmpty()) {
            throw new IllegalArgumentException("wrong user secret_key");
        }
        if (currencyRepository.findByName(userDto.getCurrency()).isEmpty()) {
            throw new IllegalArgumentException("wrong currency value");
        }
        Currency currency = currencyRepository.findByName(userDto.getCurrency()).get();
        AmountOfUserCurrency amountOfUserCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(userDto.getSecretKey(),
                        currency.getId())).get();
        if (amountOfUserCurrency.getAmount() < userDto.getCount()) {
            throw new RuntimeException("not enough balance on wallet");
        }
        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount() - userDto.getCount());
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);
        AmountOfUserCurrencyDto amountOfUserCurrencyDto = modelMapper.map(amountOfUserCurrency,
                AmountOfUserCurrencyDto.class);
        amountOfUserCurrencyDto.setCurrency(userDto.getCurrency());
        return amountOfUserCurrencyDto;
    }

    public ExchangeCurrencyDto exchangeCurrency(ExchangeCurrencyDto exchangeCurrencyDto) {
        if (userRepository.findById(exchangeCurrencyDto.getSecretKey()).isEmpty()) {
            throw new NoSuchElementException("invalid secret_key");
        }
        if (currencyRepository.findByName(exchangeCurrencyDto.getCurrencyTo()).isEmpty()) {
            throw new NoSuchElementException("invalid currency_to");
        }
        if (currencyRepository.findByName(exchangeCurrencyDto.getCurrencyFrom()).isEmpty()) {
            throw new NoSuchElementException("invalid currency_from");
        }
        Currency currency = currencyRepository.findByName(exchangeCurrencyDto.getCurrencyFrom()).get();
        AmountOfUserCurrency amountOfUserCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(exchangeCurrencyDto.getSecretKey(),
                        currency.getId())).get();
        if (amountOfUserCurrency.getAmount() < exchangeCurrencyDto.getAmount()) {
            throw new RuntimeException("not enough balance on wallet");
        }
        Currency exchangeToCurrency = currencyRepository.findByName(exchangeCurrencyDto.getCurrencyTo()).get();
        AmountOfUserCurrency amountOfUserToExchangeCurrency = amountOfUserCurrencyRepository
                .findById(new AmountOfUserCurrencyId(exchangeCurrencyDto.getSecretKey(),
                        currency.getId())).get();
        amountOfUserCurrency.setAmount(amountOfUserCurrency.getAmount() - exchangeCurrencyDto.getAmount());
        amountOfUserToExchangeCurrency.setAmount(currency.getExchangeRateToTheRuble()
                / exchangeToCurrency.getExchangeRateToTheRuble() * exchangeCurrencyDto.getAmount());
        amountOfUserCurrencyRepository.save(amountOfUserCurrency);
        amountOfUserCurrencyRepository.save(amountOfUserToExchangeCurrency);
        exchangeCurrencyDto.setAmountFrom(exchangeCurrencyDto.getAmount());
        exchangeCurrencyDto.setAmountTo(amountOfUserToExchangeCurrency.getAmount());
        return exchangeCurrencyDto;
    }
}
