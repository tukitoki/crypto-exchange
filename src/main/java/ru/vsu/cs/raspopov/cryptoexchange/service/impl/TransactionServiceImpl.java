package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.TransactionDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Role;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.repository.TransactionRepository;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.TransactionService;
import ru.vsu.cs.raspopov.cryptoexchange.utils.ValidationUtil;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public TransactionDto.Response.TransactionCounter getTransactionCount(
            TransactionDto.Request.TransactionFromTo transactionDto) {
        User user = userRepository.findById(transactionDto.getSecretKey())
                .orElseThrow(() -> new NoSuchElementException("Wrong user secret_key"));
        ValidationUtil.validUserRole(user, Role.ADMIN);

        int count = transactionRepository.getCountTransactionInTime(transactionDto.getDateFrom(),
                transactionDto.getDateTo());

        return new TransactionDto.Response.TransactionCounter(count);
    }
}
