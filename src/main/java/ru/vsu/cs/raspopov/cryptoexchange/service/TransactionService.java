package ru.vsu.cs.raspopov.cryptoexchange.service;

import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;

public interface TransactionService {

    TransactionDto.Response.TransactionCounter getTransactionCount(
            TransactionDto.Request.TransactionFromTo transactionDto);
}
