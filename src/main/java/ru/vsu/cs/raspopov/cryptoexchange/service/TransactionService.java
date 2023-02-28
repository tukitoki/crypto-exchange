package ru.vsu.cs.raspopov.cryptoexchange.service;

import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.TransactionType;

public interface TransactionService {

    TransactionDto.Response.TransactionCounter getTransactionCount(
            TransactionDto.Request.TransactionFromTo transactionDto);

    TransactionDto.Response.Transaction saveTransaction(TransactionType type, String secretKey);
}
