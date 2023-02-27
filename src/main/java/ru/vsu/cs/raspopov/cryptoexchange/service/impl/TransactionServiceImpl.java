package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Role;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.repository.TransactionRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.TransactionService;
import ru.vsu.cs.raspopov.cryptoexchange.utils.ValidationUtil;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public TransactionDto.Response.TransactionCounter getTransactionCount(
            TransactionDto.Request.TransactionFromTo transactionDto) {
        User user = userRepository.findById(UUID.fromString(transactionDto.getSecretKey()))
                .orElseThrow(() -> new NoSuchElementException("Wrong user secret_key"));
        ValidationUtil.validUserRole(user, Role.ADMIN);

        int count = transactionRepository.getCountTransactionInTime(transactionDto.getDateFrom(),
                transactionDto.getDateTo());

        log.info("ADMIN successfully get transaction count");

        return new TransactionDto.Response.TransactionCounter(count);
    }
}
