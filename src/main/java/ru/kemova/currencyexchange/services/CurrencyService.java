package ru.kemova.currencyexchange.services;

import ru.kemova.currencyexchange.dto.CurrencyDto;
import ru.kemova.currencyexchange.model.Currency;

import java.util.List;

public interface CurrencyService {
    List<CurrencyDto> findAll();

    CurrencyDto findByCode(String code);

    Currency create(Currency currency);

    void update(Currency currency);

    void delete(String code);
}
