package ru.kemova.currencyexchange.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.repository.ExchangeRateRepository;
import ru.kemova.currencyexchange.services.CurrencyService;
import ru.kemova.currencyexchange.services.ExchangeRateService;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyService currencyService;

    @Override
    @Transactional(readOnly = true)
    public List<Exchangerate> findAll() {
        return exchangeRateRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
//    public BigDecimal findByCodePair(String baseCurrency, String targetCurrency) {
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
        exchangeRateRepository.findById(id);
        log.info("fidById exchange rate : {}", id);
        return exchangeRateRepository.findById(id).orElseThrow(() ->
        {
            log.error("currency-pair not found exception thrown");
            return new CurrencyException("currency-pair not found");
        });
    }

    @Override
    @Transactional
    public Exchangerate create(ExchangeRateDto exchangeRateDto) {
        log.info("create exchange exchangeRateDto in database");

        String baseCode = exchangeRateDto.getBaseCode().toUpperCase();
        String targetCode = exchangeRateDto.getTargetCode().toUpperCase();
        BigDecimal rate = exchangeRateDto.getRate();
        if (!baseCode.equals(targetCode)) {
            Exchangerate exchangerateCreate = getExchangeRateFromDTO(rate, baseCode, targetCode);
            exchangeRateRepository.save(exchangerateCreate);
            log.info("recorder writes with ID: {}", exchangerateCreate.getId());
            return exchangerateCreate;
        } else {
            log.error("already exists exception thrown");
            throw new CurrencyException("currency-pair already exists");
        }
    }

    private Exchangerate getExchangeRateFromDTO(BigDecimal rate, String baseCode, String targetCode) {
        Exchangerate exchangerateNew = new Exchangerate();
        exchangerateNew.setRate(rate);
        exchangerateNew.setBaseCurrency(isExist(baseCode));
        exchangerateNew.setTargetCurrency(isExist(targetCode));
        return exchangerateNew;
    }

    private Currency isExist(String baseCode) {
        return currencyService.findByCode(baseCode);
    }

    @Override
    @Transactional
    public void delete(int id) {
        log.info("delete exchange rate from database : " + id);
        exchangeRateRepository.delete(findById(id));
    }
}
