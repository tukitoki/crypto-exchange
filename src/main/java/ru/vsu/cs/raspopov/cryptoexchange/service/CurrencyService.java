package ru.vsu.cs.raspopov.cryptoexchange.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.WalletOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    //TODO: сделать грамотное отображение double числа
    public List<CurrencyDto> getExchangeRate(WalletOperationDto userDto) {
        if (userRepository.findById(userDto.getSecretKey()).isEmpty()) {
            throw new NoSuchElementException("wrong secret_key");
        }
        if (currencyRepository.findByName(userDto.getCurrency()).isEmpty()) {
            throw new NoSuchElementException("wrong currency");
        }
        Currency baseCurrency = currencyRepository.findByName(userDto.getCurrency()).get();
        List<CurrencyDto> exchangeRates = new ArrayList<>();
        currencyRepository.findAll().forEach(currency -> {
            if (!currency.getName().equals(userDto.getCurrency())) {
                double exchangeRate = baseCurrency.getExchangeRateToTheRuble() / currency.getExchangeRateToTheRuble();
                exchangeRates.add(new CurrencyDto(currency.getName(),
                        BigDecimal.valueOf(exchangeRate)
                                .setScale(6, RoundingMode.HALF_UP)
                                .doubleValue()));
            }
        });
        return exchangeRates;
    }
}
