package ru.kemova;


import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.model.Exchangerate;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static final Currency currency1 = new Currency("AUD", "Australian dollar", "A$",
            new ArrayList<>(), new ArrayList<>());
    public static final String CODE = currency1.getCode();
    public static final Currency currency2 = new Currency("USD", "US dollar", "$",
            new ArrayList<>(), new ArrayList<>());
    public static final Currency currency3 = new Currency("RUB", "Russian ruble", "₽",
            new ArrayList<>(), new ArrayList<>());
    public static final Currency currency4 = new Currency("EUR", "Euro", "€",
            new ArrayList<>(), new ArrayList<>());
    public static final Currency CURRENCY_TO_CREATE = new Currency("COD", "To create", "S",
            new ArrayList<>(), new ArrayList<>());
    public static final Currency CURRENCY_TO_UPDATE = new Currency("UPD", "To update", "U",
            new ArrayList<>(), new ArrayList<>());
    public static final String BASE = currency2.getCode();
    public static final String TARGET = currency3.getCode();
    public static final List<Currency> currencies = List.of(currency1, currency2, currency3, currency4);
    public static final Exchangerate rate1 = new Exchangerate(1, 0.00, currency3, currency2);
    public static final Exchangerate rate2 = new Exchangerate(2, 0.00, currency4, currency1);
    public static final List<Exchangerate> rates = List.of(rate1, rate2);
    public static final ExchangeRateDto rateDto = new ExchangeRateDto(currency2.getCode(), currency3.getCode(), 90.59);
    public static final ExchangeRateDto RATE_TO_CREATE = new ExchangeRateDto(currency1.getCode(), currency2.getCode(), 1.0);
    public static final ExchangeRateDto RATE_TO_UPDATE = new ExchangeRateDto(currency1.getCode(), currency3.getCode(), 1.1);
    public static final String INVALID_CODE = "NOT";
    public static final int INVALID_ID = 100000;

    public static final String EXCHANGE_FROM = "USD";
    public static final String EXCHANGE_TO = "RUB";
    public static final String EXCHANGE_TO_EXCEPTION = "AUD";
    public static final int EXCHANGE_AMOUNT = 1;
    public static final double STRAIGHT_RESULT = 90.59;
    public static final double REVERSED_RESULT = 0.011;
    public static final double USD_CROSS_RESULT = 0.0098;
    public static final String EXCHANGE_CROSS_FROM = "EUR";
}
