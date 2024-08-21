package ru.kemova.currency_exchange.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currency_exchange.model.ExchangeRate;
import ru.kemova.currency_exchange.repository.CurrencyRepository;
import ru.kemova.currency_exchange.repository.ExchangeRateRepository;
import ru.kemova.currency_exchange.util.CurrencyException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    @Transactional(readOnly = true)
    public List<ExchangeRate> findAll() {
        log.info("findAll exchange rates");
        return exchangeRateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ExchangeRate findByCodePair(String baseCurrency, String targetCurrency) {
        log.info("findByCode exchange rate : " + baseCurrency + " " + targetCurrency);
        if (currencyRepository.findByCode(baseCurrency).isEmpty()
                || currencyRepository.findByCode(targetCurrency).isEmpty()) {
            log.error("currency not found");
            throw new CurrencyException("currency not found");
        } else if (exchangeRateRepository.findExchangeRateByBaseCodeAndTargetCode(baseCurrency, targetCurrency).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyException("currency-pair not found");
        }
        return exchangeRateRepository.findExchangeRateByBaseCodeAndTargetCode(baseCurrency, targetCurrency).get();
    }

    @Transactional(readOnly = true)
    public ExchangeRate findById(int id) {
        log.info("fidById exchange rate : " + id);
        if (exchangeRateRepository.findById(id).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyException("currency-pair not found");
        }
        return exchangeRateRepository.findById(id).get();
    }

    @Transactional
    public ExchangeRate create(ExchangeRate rate) {
        log.info("create exchange rate in database : " + rate.getId());
        ExchangeRate created;
        if (exchangeRateRepository.findById(rate.getId()).isEmpty())
            created = exchangeRateRepository.save(rate);
        else {
            log.error("already exists exception thrown");
            throw new CurrencyException("currency-pair already exists");
        }
        return created;
    }

    @Transactional
    public ExchangeRate update(ExchangeRate rate) {
        log.info("update exchange rate in database : " + rate.getId());
        if (exchangeRateRepository.findById(rate.getId()).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyException("currency-pair not found");
        }
        return exchangeRateRepository.save(rate);
    }

    @Transactional
    public void delete(int id) {
        log.info("delete exchange rate from database : " + id);
        Optional<ExchangeRate> exchangeRateDelete = exchangeRateRepository.findById(id);
        if (exchangeRateDelete.isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyException("currency-pair not found");
        }
        exchangeRateRepository.delete(exchangeRateDelete.get());
    }

}
