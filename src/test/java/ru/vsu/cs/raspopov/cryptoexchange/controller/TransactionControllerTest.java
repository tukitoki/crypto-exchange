package ru.vsu.cs.raspopov.cryptoexchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.TransactionService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    TransactionService transactionService;
    @InjectMocks
    TransactionController transactionController;

    @Test
    void getTransactionCount() {
        var count = new TransactionDto.Response.TransactionCounter(15);
        var fromTo = new TransactionDto.Request.TransactionFromTo("ffff",
                LocalDate.of(2022, 12, 5), LocalDate.now());

        doReturn(count).when(this.transactionService).getTransactionCount(fromTo);

        var responseEntity = this.transactionController.getTransactionCount(fromTo);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(count, responseEntity.getBody());
    }
}