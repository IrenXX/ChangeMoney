package ru.kemova.currencyexchange.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.CurrencyDtoForUpd;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.repository.CurrencyRepository;
import ru.kemova.currencyexchange.services.CurrencyService;
import ru.kemova.currencyexchange.util.CurrencyException;
import ru.kemova.currencyexchange.util.ErrorMessageEnum;

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
        log.info("findByCode currency -> {}", code);
        return currencyRepository.findByCode(code)
                .orElseThrow(() ->
                {
                    log.error("currency not found thrown exception");
                    return new CurrencyException(ErrorMessageEnum.CURRENCY_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public Currency create(Currency currency) {
        log.info("create currency in database");

        if (findByCodeForAdd(currency.getCode())) {
            log.info("recorder writes with Code -> {}", currency.getCode().toUpperCase());
            return currencyRepository.save(currency);
        } else {
            log.error("already exists exception thrown");
            throw new CurrencyException(ErrorMessageEnum.ALREADY_EXISTS);
        }
    }

    private boolean findByCodeForAdd(String code) {
        log.info("check currency for add -> {}", code.toUpperCase());
        return currencyRepository.findByCode(code.toUpperCase()).isEmpty();
    }

    @Override
    @Transactional
    public void update(String code, CurrencyDtoForUpd currencyUpd) {
        if (findByCodeForUpd(code)) {
            log.info("update currency in database -> {}", code);
            Currency currency = getCurrencyFromDto(code, currencyUpd);
            currencyRepository.save(currency);
        } else {
            throw new CurrencyException(ErrorMessageEnum.CODE_NOT_IN_ADDRESS);
        }
    }

    private Currency getCurrencyFromDto(String code, CurrencyDtoForUpd currencyUpd) {
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setFullName(currencyUpd.getFullName());
        currency.setSign(currencyUpd.getSign());
        return currency;
    }

    private boolean findByCodeForUpd(String code) {
        log.info("findByCode currency for update -> {}", code);
        return currencyRepository.findByCode(code).isPresent();
    }

    @Override
    @Transactional
    public void delete(String code) {
        currencyRepository.delete(findByCode(code));
        log.info("delete currency from database -> {}", code);
    }
}
