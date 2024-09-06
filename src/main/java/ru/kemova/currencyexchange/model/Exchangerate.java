package ru.kemova.currencyexchange.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Exchangerate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull (message = "Please enter rate")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "base_currency_code")
    @ToString.Exclude
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "target_currency_code")
    @ToString.Exclude
    private Currency targetCurrency;
}
