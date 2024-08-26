package ru.kemova.currencyexchange.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.repository.CurrencyRepository;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.util.List;

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
        return currencyRepository.findByCode(code).orElseThrow(() ->
        {
            log.error("currency not found thrown exception");
            return new CurrencyException("currency not found");
        });
    }

    @Override
    @Transactional
    public Currency create(Currency currency) {
        log.info("create currency in database");

        if (currencyRepository.findByCode(currency.getCode()).isEmpty()) {
            log.info("recorder writes with Code: " + currency.getCode());
            return currencyRepository.save(currency);
        } else {
            log.error("already exists exception thrown");
            throw new CurrencyException("currency already exists");
        }
    }

    @Override
    @Transactional
    public void update(Currency currency) {
        if (currencyRepository.findByCode(currency.getCode()).isEmpty()) {
            log.error("currency not found thrown exception");
            throw new CurrencyException("currency not found");
        }
        log.info("update currency in database : " + currency.getCode());
        currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public void delete(String code) {
        Currency currencyDelete = currencyRepository.findByCode(code).orElseThrow(() ->
        {
            log.error("currency not found thrown exception");
            return new CurrencyException("currency not found");
        });
        log.info("delete currency from database : " + code);
        currencyRepository.delete(currencyDelete);
    }
}
