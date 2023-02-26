package ru.vsu.cs.raspopov.cryptoexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootApplication
public class CryptoExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoExchangeApplication.class, args);
    }

}
