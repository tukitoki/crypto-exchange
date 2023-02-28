package ru.vsu.cs.raspopov.cryptoexchange.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping(path = "transaction/count",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<TransactionDto.Response.TransactionCounter> getTransactionCount(
            @RequestBody @NotNull @Valid TransactionDto.Request.TransactionFromTo transactionDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getTransactionCount(transactionDto));
    }
}
