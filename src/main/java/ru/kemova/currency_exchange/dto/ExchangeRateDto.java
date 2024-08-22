package ru.kemova.currency_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kemova.currency_exchange.model.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {

    private Integer id;
    private String baseCode;
    private String targetCode;
    private double rate;
}
