package ru.kemova.currency_exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kemova.currency_exchange.model.ExchangeRate;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {
    Optional<ExchangeRate> findExchangeRateByBaseCodeAndTargetCode(String to, String from);
}
