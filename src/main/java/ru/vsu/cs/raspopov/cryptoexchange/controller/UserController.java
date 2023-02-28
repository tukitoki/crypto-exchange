package ru.vsu.cs.raspopov.cryptoexchange.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserRegistrationDto;
import ru.vsu.cs.raspopov.cryptoexchange.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    @PostMapping(path = "user",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createUser(
            @RequestBody @NotNull @Valid UserRegistrationDto userRegistrationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userRegistrationDto));
    }
}
