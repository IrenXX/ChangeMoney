package ru.kemova.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {

    private String code;
    private String fullName;
    private String sign;
}
