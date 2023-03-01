package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.entity.enums.Role;
import ru.vsu.cs.raspopov.cryptoexchange.repository.TransactionRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.TransactionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
        User user = new User(
                UUID.fromString("f24de643-ace3-4224-8534-681d6c329aca"),
                "test",
                "test@mail.ru",
                Role.ADMIN,
                new ArrayList<>()
        );
        int count = 15;
        var counter = new TransactionDto.Response.TransactionCounter(count);
        var fromTo = new TransactionDto.Request.TransactionFromTo(
                "f24de643-ace3-4224-8534-681d6c329aca",
                LocalDate.of(2022, 12, 5), LocalDate.now());

        when(userRepository.findById(user.getSecretKey()))
                .thenReturn(Optional.of(user));
        when(transactionRepository.getCountTransactionInTime(fromTo.getDateFrom(),
                fromTo.getDateTo())).thenReturn(count);

        var expected = transactionService.getTransactionCount(fromTo);
        assertEquals(counter, expected);
    }
}