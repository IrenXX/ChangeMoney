package ru.kemova.currencyexchange.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CurrencyException extends RuntimeException {
    private final ErrorMessageEnum error;
}