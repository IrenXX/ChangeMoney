package ru.kemova.currency_exchange.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String code;
    @NotBlank
    private String fullName;
    @NotBlank
    private String sign;
//
//    @OneToMany(mappedBy = "baseCurrency", cascade = CascadeType.ALL)
//    private List<ExchangeRate> exchangeBaseRates = new ArrayList<>();
//
//    @OneToMany(mappedBy = "targetCurrency", cascade = CascadeType.ALL)
//    private List<ExchangeRate> exchangeTargetRates = new ArrayList<>();
}
