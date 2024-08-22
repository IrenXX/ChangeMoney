package ru.kemova.currency_exchange.services;

import ru.kemova.currency_exchange.model.ExchangeRate;

import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRate> findAll();

    ExchangeRate findByCodePair(String baseCurrency, String targetCurrency);

    ExchangeRate create(ExchangeRate rate);

    void update(ExchangeRate rate);

    void delete(int id);
}
