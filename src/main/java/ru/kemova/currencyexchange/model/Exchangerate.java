package ru.kemova.currencyexchange.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Exchangerate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private BigDecimal rate;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency_code")
    @JsonIgnore
    private Currency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_currency_code")
    @JsonIgnore
    private Currency targetCurrency;
}
