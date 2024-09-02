package ru.kemova.currencyexchange.services;

import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Exchangerate;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRateDto> findAll();

    BigDecimal findByCodePair(String baseCurrency, String targetCurrency);

    Exchangerate create(ExchangeRateDto rateDto);

    void delete(int id);
}
