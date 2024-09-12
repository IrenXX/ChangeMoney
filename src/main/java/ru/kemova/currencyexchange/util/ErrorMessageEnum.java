package ru.kemova.currencyexchange.util;

import org.springframework.http.HttpStatus;

public enum ErrorMessageEnum {
    CURRENCY_NOT_FOUND("Currency not found", HttpStatus.NOT_FOUND),
    CODE_NOT_IN_ADDRESS("Currency code is not in address", HttpStatus.BAD_REQUEST),
    EMPTY_FORM_FIELD("Required form field is missing", HttpStatus.BAD_REQUEST),
    ALREADY_EXISTS("Currency with this code already exists", HttpStatus.CONFLICT),
    PAIR_EXCHANGE_RATE_NOT_FOUND("Currency pair exchange rate is not found", HttpStatus.NOT_FOUND),
    PAIR_NOT_FOUND("Currency pair is not found", HttpStatus.NOT_FOUND),
    PAIR_ALREADY_EXISTS("Currency pair already exists", HttpStatus.CONFLICT),
    DATA_IS_INVALID("Input data is invalid", HttpStatus.BAD_REQUEST),
    ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR);;

    private final String message;
    private final HttpStatus status;

    ErrorMessageEnum(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
