package ru.kemova.currency_exchange.services;

import ru.kemova.currency_exchange.model.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> findAll();

    Currency findById(Integer id);

    Currency findByCode(String code);

    void create(Currency currency);

    void update(Currency currency);

    void delete(int id);
}
