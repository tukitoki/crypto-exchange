package ru.vsu.cs.raspopov.cryptoexchange.controller;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.CurrencyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("currency/exchange-rate")
    public ResponseEntity<List<CurrencyDto.Response.CurrencyExchangeDto>> getExchangeRate(
            @RequestBody @NotNull CurrencyDto.Request.SecretKeyCurrencyDto currencyDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(currencyService.getExchangeRate(currencyDto));
    }

    @PostMapping("currency/change-exchange-rate")
    public ResponseEntity<List<CurrencyDto.Response.CurrencyExchangeDto>> changeExchangeRates(
            @RequestBody @NotNull CurrencyDto.Request.ChangeExchangeRateDto currencyDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(currencyService.updateExchangeRates(currencyDto));
    }

    @GetMapping("currency/total-currency-amount")
    public ResponseEntity<AmountOfUserCurrencyDto.Response.CurrencyAmount> getTotalAmountOfCurrency(
            @RequestBody @NotNull CurrencyDto.Request.SecretKeyCurrencyDto currencyDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(currencyService.getTotalAmountOfCurrency(currencyDto));
    }
}
