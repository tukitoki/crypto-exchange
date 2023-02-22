package ru.vsu.cs.raspopov.cryptoexchange.repository;


import org.springframework.data.repository.CrudRepository;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);
}
