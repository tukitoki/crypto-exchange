package ru.vsu.cs.raspopov.cryptoexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vsu.cs.raspopov.cryptoexchange.entity.UserCurrencyAmount;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String secret_key;

}
