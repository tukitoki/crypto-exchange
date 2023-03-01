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
import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.TransactionService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @ApiOperation(value = "Returns count of transactions that were made in the interval of the transferred dates")
    @GetMapping(path = "transaction/count",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TransactionDto.Response.TransactionCounter> getTransactionCount(
            @RequestBody @NotNull(message = "Type secret_key, date_from and date_to") @Valid
            @ApiParam(value = "User secret_key, date_from and date_to - interval")
            TransactionDto.Request.TransactionFromTo transactionDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getTransactionCount(transactionDto));
    }
}
