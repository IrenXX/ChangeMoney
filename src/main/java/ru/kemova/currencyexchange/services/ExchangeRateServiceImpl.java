package ru.kemova.currencyexchange.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.repository.CurrencyRepository;
import ru.kemova.currencyexchange.repository.ExchangeRateRepository;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Exchangerate> findAll() {
        log.info("findAll exchange rates");
        return exchangeRateRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public double findByCodePair(String baseCurrency, String targetCurrency) {
        log.info("findByCodePair exchange rate : {}, {}", baseCurrency, targetCurrency);

        return exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrency, targetCurrency)
                .orElseThrow(() ->
                {
                    log.error("not found exception thrown");
                    throw new CurrencyException("currency-pair not found");
                }).getRate();
    }

    //    @Transactional(readOnly = true)
//    public ExchangeRate findById(int id) {
//    Optional <ExchangeRate> getExchangeRate = exchangeRateRepository.findById(id);
//        log.info("fidById exchange rate : " + id);
//        return exchangeRateRepository.findById(id).get().orElseThrow(()->
//            {
    //            log.error("not found exception thrown");
    //            return new CurrencyException("currency-pair not found");
//            };);
//    }

    @Override
    @Transactional
    public Exchangerate create(ExchangeRateDto rateDto) {
        log.info("create exchange rate in database");
        if (isExist(rateDto.getBaseCode()) && !rateDto.getBaseCode().equals(rateDto.getTargetCode())) {
            Exchangerate rateCreate = getExchangeRateFromDTO(rateDto);
            exchangeRateRepository.save(rateCreate);
            log.info("recorder writes with ID: " + rateCreate.getId());
            return rateCreate;
        } else {
            log.error("already exists exception thrown");
            throw new CurrencyException("currency-pair already exists");
        }
    }

    private boolean isExist(String baseCode) {
        return currencyRepository.findByCode(baseCode).isPresent();
    }

    @Override
    @Transactional
    public void delete(int id) {
        log.info("delete exchange rate from database : " + id);
        Exchangerate exchangeRateDelete = exchangeRateRepository.findById(id).orElseThrow(() ->
        {
            log.error("not found exception thrown");
            throw new CurrencyException("currency-pair not found");
        });
        exchangeRateRepository.delete(exchangeRateDelete);
    }

    public Exchangerate getExchangeRateFromDTO(ExchangeRateDto rate) {
        Exchangerate to = new Exchangerate();
        to.setBaseCurrency(currencyRepository.findByCode(rate.getBaseCode())
                .orElseThrow(() -> new CurrencyException("Currency (base code) should be not null")));
        to.setTargetCurrency(currencyRepository.findByCode(rate.getTargetCode())
                .orElseThrow(() -> new CurrencyException("Currency (target code) should be not null")));
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
