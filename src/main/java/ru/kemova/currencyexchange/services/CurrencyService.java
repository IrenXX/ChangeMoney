package ru.kemova.currencyexchange.services;

import ru.kemova.currencyexchange.dto.CurrencyDtoForUpd;
import ru.kemova.currencyexchange.model.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> findAll();

    Currency findByCode(String code);

    Currency create(Currency currency);

    void update(String code, CurrencyDtoForUpd currency);

    void delete(String code);
}
