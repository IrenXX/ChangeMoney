package ru.kemova.currencyexchange.services;

import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Exchangerate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateService {

    List<Exchangerate> findAll();

    Exchangerate findByCodePair(String baseCurrency, String targetCurrency);

    Optional<Exchangerate> findByCodePairForExchange(String baseCurrency, String targetCurrency);

    Exchangerate create(ExchangeRateDto rateDto);

    Exchangerate update(int id, ExchangeRateDto rateDto);


    void delete(int id);
}
