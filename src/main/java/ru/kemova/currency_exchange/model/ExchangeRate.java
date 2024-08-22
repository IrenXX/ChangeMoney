package ru.kemova.currency_exchange.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency_id")
    private Currency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_currency_id")
    private Currency targetCurrency;

    @NotBlank
    private double rate;

}
