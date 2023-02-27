package ru.vsu.cs.raspopov.cryptoexchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.cs.raspopov.cryptoexchange.dto.AmountOfUserCurrencyDto;
import ru.vsu.cs.raspopov.cryptoexchange.entity.User;
import ru.vsu.cs.raspopov.cryptoexchange.service.BalanceService;

import javax.swing.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

    @Mock
    BalanceService balanceService;

    @InjectMocks
    BalanceController balanceController;

//    @Test
//    void walletBalance_ReturnsValidResponseEntity() {
//        //given
//        var userWallet = List.of(new AmountOfUserCurrencyDto.Response.CurrencyAmount("RUB", 0),
//                new AmountOfUserCurrencyDto.Response.CurrencyAmount("TON", 1000),
//                new AmountOfUserCurrencyDto.Response.CurrencyAmount("BTC", 0.523));
//        var user = new User()
//
//        Mockito.doReturn(userWallet).when(balanceService.getUserBalance());
//        //when
//
//        //then
//    }

}