package ru.vsu.cs.raspopov.cryptoexchange.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.BalanceOperationDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.BalanceService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class BalanceController {

    private final BalanceService balanceService;

    @ApiOperation(value = "Get your wallet balance")
    @GetMapping(path = "balance",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<AmountOfUserCurrencyDto.Response.CurrencyAmount>> walletBalance(
            @RequestBody @NotNull(message = "type secret_key") @Valid
            @ApiParam(value = "User secret_key") UserDto.Request.UserSecretKey userDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balanceService.getUserBalance(userDto));
    }

    @ApiOperation(value = "Replenishment of the wallet balance")
    @PostMapping(path = "balance/replenishment",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AmountOfUserCurrencyDto.Response.CurrencyAmount> replenishmentBalance(
            @RequestBody @NotNull(message = "type secret_key, currency and amount of replenishment")
            @Valid @ApiParam(value = "User secret_key, currency name and amount to replenishment")
            BalanceOperationDto.Request.ReplenishmentBalance replenishmentBalance) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balanceService.replenishmentBalance(replenishmentBalance));
    }

    @ApiOperation(value = "Withdrawal money from wallet")
    @PostMapping(path = "balance/withdrawal",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AmountOfUserCurrencyDto.Response.CurrencyAmount> withdrawalMoney(
            @RequestBody @NotNull(message = "type secret_key, currency, amount and platform for withdrawal")
            @Valid @ApiParam(value = "User secret_key, currency name, amount to withdraw, " +
                    "platform to withdrawal") BalanceOperationDto.Request.WithdrawalBalance withdrawalBalance) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balanceService.withdrawalMoney(withdrawalBalance));
    }

    @ApiOperation(value = "Currency exchange with one to an another")
    @PostMapping(path = "balance/exchange",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<BalanceOperationDto.Response.ExchangeCurrency> exchangeCurrency(
            @RequestBody @NotNull(message = "type secret_key, currency_from, currency_to and amount to exchange")
            @Valid @ApiParam(value = "User secret_key, currency_from exchange, currency_to exchange" +
                    "and amount of exchanged currency")
            BalanceOperationDto.Request.ExchangeCurrency exchangeCurrency) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balanceService.exchangeCurrency(exchangeCurrency));
    }
}
