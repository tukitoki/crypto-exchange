package ru.vsu.cs.raspopov.cryptoexchange.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.BalanceOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.BalanceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("balance")
    public ResponseEntity<List<AmountOfUserCurrencyDto.Response.CurrencyAmount>> walletBalance(
            @RequestBody @NotNull @Valid UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balanceService.getUserBalance(userDto));
    }

    @PostMapping("balance/replenishment")
    public ResponseEntity<AmountOfUserCurrencyDto.Response.CurrencyAmount> replenishmentBalance(
            @RequestBody @NotNull @Valid BalanceOperationDto.Request.ReplenishmentBalance replenishmentBalance) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balanceService.replenishmentBalance(replenishmentBalance));
    }

    @PostMapping("balance/withdrawal")
    public ResponseEntity<AmountOfUserCurrencyDto.Response.CurrencyAmount> withdrawalMoney(
            @RequestBody @NotNull @Valid BalanceOperationDto.Request.WithdrawalBalance withdrawalBalance) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balanceService.withdrawalMoney(withdrawalBalance));
    }

    @PostMapping("balance/exchange")
    public ResponseEntity<BalanceOperationDto.Response.ExchangeCurrency> exchangeCurrency(
            @RequestBody @NotNull @Valid BalanceOperationDto.Request.ExchangeCurrency exchangeCurrency) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balanceService.exchangeCurrency(exchangeCurrency));
    }
}