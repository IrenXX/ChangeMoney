package ru.kemova.currency_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kemova.currency_exchange.model.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDto {

    private Currency base;
    private Currency target;
    private double rate;
    private double amount;
    private double convertedAmount;
}
