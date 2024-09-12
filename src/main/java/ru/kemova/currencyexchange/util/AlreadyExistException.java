package ru.kemova.currencyexchange.util;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }
}