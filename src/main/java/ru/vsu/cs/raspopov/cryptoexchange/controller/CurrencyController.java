package ru.vsu.cs.raspopov.cryptoexchange.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.CurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.CurrencyService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class CurrencyController {

    private final CurrencyService currencyService;

    @ApiOperation(value = "Returns total of given currency amount")
    @GetMapping(path = "currency/total-amount",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AmountOfUserCurrencyDto.Response.CurrencyAmount> getTotalAmountOfCurrency(
            @RequestBody @NotNull(message = "Type secret_key and currency name") @Valid
            @ApiParam(value = "User secret_key and currency name of which must be returned")
            CurrencyDto.Request.SecretKeyCurrency currencyDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(currencyService.getTotalAmountOfCurrency(currencyDto));
    }
}
