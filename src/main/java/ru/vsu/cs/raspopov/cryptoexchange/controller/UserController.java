package ru.vsu.cs.raspopov.cryptoexchange.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserRegistrationDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    @PostMapping("user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createUser(
            @RequestBody @NotNull @Valid UserRegistrationDto userRegistrationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userRegistrationDto));
    }

}
