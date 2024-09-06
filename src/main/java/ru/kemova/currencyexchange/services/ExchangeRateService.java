package ru.kemova.currencyexchange.services;

import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Exchangerate;

import java.util.List;

public interface ExchangeRateService {

    List<Exchangerate> findAll();

//    BigDecimal findByCodePair(String baseCurrency, String targetCurrency);
    Exchangerate findByCodePair(String baseCurrency, String targetCurrency);

    Exchangerate create(ExchangeRateDto rateDto);

    void delete(int id);
}
