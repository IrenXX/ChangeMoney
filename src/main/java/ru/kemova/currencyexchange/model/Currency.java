package ru.kemova.currencyexchange.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "baseCurrency", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Exchangerate> exchangeBaseRates = new ArrayList<>();

    @OneToMany(mappedBy = "targetCurrency", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Exchangerate> exchangeTargetRates = new ArrayList<>();
}
