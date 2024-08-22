package ru.kemova.currency_exchange.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currency_exchange.model.Currency;
import ru.kemova.currency_exchange.repository.CurrencyRepository;
import ru.kemova.currency_exchange.util.CurrencyException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {
        log.info("findAll currencies");
        return currencyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Currency findByCode(String code) {
        log.info("findByCode currency : " + code);
        if (currencyRepository.findByCode(code).isEmpty()) {
            log.error("currency not found thrown exception");
            throw new CurrencyException("currency not found");
        }
        return currencyRepository.findByCode(code).get();
    }

    @Transactional(readOnly = true)
    @Override
    public Currency findById(Integer id) {
        log.info("getById currency : " + id);
        if (currencyRepository.findById(id).isEmpty()) {
            log.error("currency not found thrown exception");
            throw new CurrencyException("currency not found");
        }
        return currencyRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void create(Currency currency) {
        log.info("create currency in database");
        Currency created;

        if (currencyRepository.findByCode(currency.getCode()).isEmpty()) {
            created = currencyRepository.save(currency);
            log.info("recorder writes with ID: " + currency.getId());
        } else {
            log.error("already exists exception thrown");
            throw new CurrencyException("currency already exists");
        }
    }

    @Transactional
    @Override
    public void update(Currency currency) {
        log.info("update currency in database : " + currency.getId());
        if (currencyRepository.findById(currency.getId()).isEmpty()) {
            log.error("currency not found thrown exception");
            throw new CurrencyException("currency not found");
        }
        currencyRepository.save(currency);
    }

    @Transactional
    @Override
    public void delete(int id) {
        log.info("delete currency from database : " + id);
        Optional<Currency> currencyDelete = currencyRepository.findById(id);
        if (currencyDelete.isEmpty()) {
            log.error("currency not found thrown exception");
            throw new CurrencyException("currency not found");
        }
        currencyRepository.delete(currencyDelete.get());
    }
}
