package ru.kemova.currencyexchange.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.CurrencyDto;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.repository.CurrencyRepository;
import ru.kemova.currencyexchange.services.CurrencyService;
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
        return currencyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Currency findByCode(String code) {
        log.info("findByCode currency : {}", code);
        return currencyRepository.findByCode(code)
                .orElseThrow(() ->
                {
                    log.error("currency not found thrown exception");
                    return new CurrencyException("currency not found");
                });
    }

    @Override
    @Transactional
    public Currency create(Currency currency) {
        log.info("create currency in database");

        if (findByCode(currency.getCode()) == null) {
            log.info("recorder writes with Code: {}", currency.getCode());
            return currencyRepository.save(currency);
        } else {
            log.error("already exists exception thrown");
            throw new CurrencyException("currency already exists");
        }
    }

    @Override
    @Transactional
    public void update(Currency currency) {
        findByCode(currency.getCode());
        log.info("update currency in database : {}", currency.getCode());
        currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public void delete(String code) {
        currencyRepository.delete(findByCode(code));
        log.info("delete currency from database : {}", code);
    }
}
