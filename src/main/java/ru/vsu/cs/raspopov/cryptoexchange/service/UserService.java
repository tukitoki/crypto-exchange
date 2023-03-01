package ru.vsu.cs.raspopov.cryptoexchange.service;

import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;

public interface UserService {

    UserDto.Response.UserSecretKey createUser(UserDto.Request.UserRegistration userRegistrationDto);
}
