package ru.vsu.cs.raspopov.cryptoexchange.repository;


import org.springframework.data.repository.CrudRepository;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String email);
}
