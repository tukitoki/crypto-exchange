package ru.vsu.cs.raspopov.cryptoexchange.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserDto;
import ru.vsu.cs.raspopov.cryptoexchange.dto.UserRegistrationDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.Role;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("email already present");
        } else if (userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("username already present");
        }
        User user = modelMapper.map(userRegistrationDto, User.class);
        user = userRepository.save(user);
        user.setRole(Role.USER);
        return modelMapper.map(user, UserDto.class);
    }
}
