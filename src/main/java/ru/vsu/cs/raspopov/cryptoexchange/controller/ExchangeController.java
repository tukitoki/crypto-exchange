package ru.vsu.cs.raspopov.cryptoexchange.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.ExchangeService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @ApiOperation(value = "Returns exchange courses depending on the basic currency")
    @GetMapping(path = "exchange/rate",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<CurrencyDto.Response.CurrencyExchange>> getExchangeRate(
            @RequestBody @NotNull(message = "type secret_key and currency name") @Valid
            @ApiParam(value = "User secret_key and currency name") CurrencyDto.Request.SecretKeyCurrency currencyDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exchangeService.getExchangeRate(currencyDto));
    }

    @ApiOperation(value = "Change in currency exchange rates depending on the basic currency")
    @PostMapping(path = "exchange/change-rate",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<CurrencyDto.Response.CurrencyExchange>> changeExchangeRates(
            @RequestBody @NotNull(message = "type secret_key, currency name and currencies") @Valid
            @ApiParam(value = "User secret_key, base_currency name and currencies whose rates should be changed")
            CurrencyDto.Request.ChangeExchangeRate currencyDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exchangeService.updateExchangeRates(currencyDto));
    }

}
