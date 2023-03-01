package ru.vsu.cs.raspopov.cryptoexchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.mapper.UserMapper;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;
import ru.vsu.cs.raspopov.cryptoexchange.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto.Response.UserSecretKey createUser(UserDto.Request.UserRegistration userRegistrationDto) {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already present");
        } else if (userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already present");
        }

        User user = userMapper.toEntity(userRegistrationDto);
        userRepository.save(user);

        log.info("USER successfully registered");

        return new UserDto.Response.UserSecretKey(user.getSecretKey().toString());
    }
}
