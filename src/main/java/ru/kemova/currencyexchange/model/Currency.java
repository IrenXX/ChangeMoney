package ru.kemova.currencyexchange.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "code")
@ToString
public class Currency {

    @Id
    private String code;

    @NotBlank
    private String fullName;

    @NotBlank
    private String sign;

    @OneToMany(mappedBy = "baseCurrency", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Exchangerate> exchangeBaseRates;

    @OneToMany(mappedBy = "targetCurrency", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Exchangerate> exchangeTargetRates;
}
