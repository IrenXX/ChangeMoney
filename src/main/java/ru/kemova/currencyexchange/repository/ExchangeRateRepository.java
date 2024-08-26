package ru.kemova.currencyexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kemova.currencyexchange.model.Exchangerate;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<Exchangerate, Integer> {
    Optional<Exchangerate> findByBaseCurrencyCodeAndTargetCurrencyCode(String to, String from);
}
