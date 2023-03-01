package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Currency;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Transaction;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.TransactionType;
import ru.vsu.cs.raspopov.cryptoexchange.repository.CurrencyRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.ExchangeRateRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.TransactionRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.ExchangeService;
import ru.vsu.cs.raspopov.cryptoexchange.service.TransactionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    void getTransactionCount() {
        int count = 15;
        var counter = new TransactionDto.Response.TransactionCounter(count);
        var fromTo = new TransactionDto.Request.TransactionFromTo(
                "f24de643-ace3-4224-8534-681d6c329aca",
                LocalDate.of(2022, 12, 5), LocalDate.now());


        when(transactionRepository.getCountTransactionInTime(fromTo.getDateFrom(),
                fromTo.getDateTo())).thenReturn(count);

        var expected = transactionService.getTransactionCount(fromTo);
        assertEquals(counter, expected);
    }

    @Test
    void saveTransaction() {
        Transaction transaction = new Transaction(1,
                "f24de643-ace3-4224-8534-681d6c329aca", TransactionType.EXCHANGE,
                LocalDate.now());

        when(transactionRepository.save(new Transaction(transaction.getSecretKey(),
                transaction.getType(), LocalDate.now())))
                .thenReturn(transaction);
        var expected = new TransactionDto.Response.Transaction(transaction.getSecretKey(),
                transaction.getDate());

        var trans = transactionService.saveTransaction(TransactionType.EXCHANGE,
                "f24de643-ace3-4224-8534-681d6c329aca");
        assertEquals(expected, trans);
    }
}