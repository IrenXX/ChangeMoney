package ru.kemova.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kemova.currencyexchange.model.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDto {

//    private Currency base;
//    private Currency target;
//    private double rate;
//    private double amount;
//    private double convertedAmount;

    private String from;
    private String to;
    private int amount;
}
