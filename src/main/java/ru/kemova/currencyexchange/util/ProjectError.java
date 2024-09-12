package ru.kemova.currencyexchange.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectError {

    private int status;
    ErrorMessageEnum error;
    private String message;

}
