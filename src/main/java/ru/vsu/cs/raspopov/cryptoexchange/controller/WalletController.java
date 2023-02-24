package ru.vsu.cs.raspopov.cryptoexchange.controller;

import com.sun.net.httpserver.HttpsServer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.raspopov.cryptoexchange.dto.ExchangeCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.WalletOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.WalletService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("balance")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AmountOfUserCurrencyDto>> walletBalance(@RequestBody @NotNull @Valid UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletService.getUserBalance(userDto));
    }

    @PostMapping("balance/replenishment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AmountOfUserCurrencyDto> replenishmentBalance(
            @RequestBody @NotNull @Valid WalletOperationDto userDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletService.replenishmentBalance(userDto));
    }

    @PostMapping("balance/withdrawal")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AmountOfUserCurrencyDto> withdrawalMoney(
            @RequestBody @NotNull @Valid WalletOperationDto userDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletService.withdrawalMoney(userDto));
    }

    @PostMapping("balance/exchange")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExchangeCurrencyDto> exchangeCurrency(
            @RequestBody @NotNull @Valid ExchangeCurrencyDto exchangeCurrencyDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(walletService.exchangeCurrency(exchangeCurrencyDto));
    }
}
