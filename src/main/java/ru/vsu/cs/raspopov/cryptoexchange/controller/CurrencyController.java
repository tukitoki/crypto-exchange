package ru.vsu.cs.raspopov.cryptoexchange.controller;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.WalletOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.CurrencyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("currency/exchange_rate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CurrencyDto>> getExchangeRate(
            @RequestBody @NotNull WalletOperationDto userDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(currencyService.getExchangeRate(userDto));
    }
}
