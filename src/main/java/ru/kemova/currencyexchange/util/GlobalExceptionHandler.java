package ru.kemova.currencyexchange.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {//} extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CurrencyException.class) //404
    public ResponseEntity<ProjectError> handleNotFound(CurrencyException exception) {
        return new ResponseEntity<>(new ProjectError(exception.getError().getStatus().value(),
                exception.getError(), exception.getError().getMessage()), exception.getError().getStatus());
    }

    @ExceptionHandler(Exception.class) //500
    public ResponseEntity<String> handleException() {
        return new ResponseEntity<>("Ошибка в теле запроса", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                          WebRequest request) {

        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

