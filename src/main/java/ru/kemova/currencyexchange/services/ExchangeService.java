package ru.kemova.currencyexchange.services;

import ru.kemova.currencyexchange.dto.ExchangeResponseDto;

import java.math.BigDecimal;

public interface ExchangeService {

    ExchangeResponseDto exchange(String from, String to, BigDecimal amount);
}
