package ru.kemova.currencyexchange.services;

public interface ExchangeService {

    double exchange(String from, String to, int amount);
}
