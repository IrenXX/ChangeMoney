package ru.kemova.currencyexchange.services;

import java.math.BigDecimal;

public interface ExchangeService {

    BigDecimal exchange(String from, String to, BigDecimal amount);
}
