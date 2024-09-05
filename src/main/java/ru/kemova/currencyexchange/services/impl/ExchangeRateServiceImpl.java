package ru.kemova.currencyexchange.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.repository.ExchangeRateRepository;
import ru.kemova.currencyexchange.services.CurrencyService;
import ru.kemova.currencyexchange.services.ExchangeRateService;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyService currencyService;

    @Override
    @Transactional(readOnly = true)
    public List<Exchangerate> findAll() {
        log.info("findAll exchange rates");
        return exchangeRateRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Exchangerate findByCodePair(String baseCurrency, String targetCurrency) {
        log.info("findByCodePair exchange rate : {}, {}", baseCurrency, targetCurrency);

        return exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrency, targetCurrency)
                .orElseThrow(() ->
                {
                    log.error("currency-pair not found exception thrown");
                    throw new CurrencyException("currency-pair not found");
                });
    }

    @Transactional(readOnly = true)
    public Exchangerate findById(int id) {
        Optional<Exchangerate> getExchangeRate = exchangeRateRepository.findById(id);
        log.info("fidById exchange rate : {}", id);
        return exchangeRateRepository.findById(id).orElseThrow(() ->
        {
            log.error("not found exception thrown");
            return new CurrencyException("currency not found");
        });
    }

    @Override
    @Transactional
    public Exchangerate create(ExchangeRateDto rateDto) {
        log.info("create exchange rate in database");
        if (isExist(rateDto.getBaseCode()) && !rateDto.getBaseCode().equals(rateDto.getTargetCode())) {
            Exchangerate rateCreate = getExchangeRateFromDTO(rateDto);
            exchangeRateRepository.save(rateCreate);
            log.info("recorder writes with ID: {}", rateCreate.getId());
            return rateCreate;
        } else {
            log.error("already exists exception thrown");
            throw new CurrencyException("currency-pair already exists");
        }
    }

    private boolean isExist(String baseCode) {
         currencyService.findByCode(baseCode);
         return true;
    }

    @Override
    @Transactional
    public void delete(int id) {
        log.info("delete exchange rate from database : {}", id);
        exchangeRateRepository.delete(findById(id));
    }

    public Exchangerate getExchangeRateFromDTO(ExchangeRateDto rate) {
        Exchangerate to = new Exchangerate();
        to.setBaseCurrency(currencyService.findByCode(rate.getBaseCode()));
        to.setTargetCurrency(currencyService.findByCode(rate.getTargetCode()));
        to.setRate(rate.getRate());
        log.info("get transfer object");
        return to;
    }

//    @Transactional
//    public void update(Exchangerate rate) {
//        log.info("update exchange rate in database with ID: " + rate.getId());
//        if (exchangeRateRepository.findById(rate.getId()).isEmpty()) {
//            log.error("not found exception thrown");
//            throw new CurrencyException("currency-pair not found");
//        }
//        exchangeRateRepository.save(rate);

//    }
}
