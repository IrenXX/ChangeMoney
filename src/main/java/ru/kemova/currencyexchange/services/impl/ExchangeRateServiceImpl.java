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
import ru.kemova.currencyexchange.util.ErrorMessageEnum;

import java.math.BigDecimal;
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
//        List<Exchangerate> all = exchangeRateRepository.findAll();
//        List<ExchangeRateDto> dtoList = new ArrayList<>();
//        for (Exchangerate exchangerate : all) {
//            ExchangeRateDto rateDto = new ExchangeRateDto();
//            rateDto.setBaseCode(exchangerate.getBaseCurrency().getCode());
//            rateDto.setTargetCode(exchangerate.getTargetCurrency().getCode());
//            rateDto.setRate(exchangerate.getRate());
//            dtoList.add(rateDto);
//        }
//        return dtoList;
        return exchangeRateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Exchangerate findById(int id) {
        log.info("fidById exchange rate -> {}", id);
        return exchangeRateRepository.findById(id).orElseThrow(() ->
        {
            log.error("currency-pair not found exception thrown");
            return new CurrencyException(ErrorMessageEnum.PAIR_NOT_FOUND);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Exchangerate findByCodePair(String baseCurrency, String targetCurrency) {
        log.info("findByCodePair exchange rate : {}, {}", baseCurrency, targetCurrency);

        return exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrency, targetCurrency)
                .orElseThrow(() ->
                {
                    log.error("currency-pair not found exception thrown");
                    throw new CurrencyException(ErrorMessageEnum.PAIR_EXCHANGE_RATE_NOT_FOUND);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Exchangerate> findByCodePairForExchange(String baseCurrency, String targetCurrency) {
        log.info("findByCodePair exchange rate : {}, {}", baseCurrency, targetCurrency);
        return exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrency, targetCurrency);
    }

    @Override
    @Transactional
    public Exchangerate create(ExchangeRateDto exchangeRateDto) {
        log.info("create exchange exchangeRateDto in database");

        String baseCode = exchangeRateDto.getBaseCode().toUpperCase();
        String targetCode = exchangeRateDto.getTargetCode().toUpperCase();
        BigDecimal rate = exchangeRateDto.getRate();
        if (!baseCode.equals(targetCode) && findByCodePairForAdd(baseCode, targetCode)) {
            Exchangerate exchangeRateCreate = getExchangeRateFromDTO(rate, baseCode, targetCode);
            exchangeRateRepository.save(exchangeRateCreate);
            log.info("recorder writes with ID: {}", exchangeRateCreate.getId());
            return exchangeRateCreate;
        } else {
            log.error("already exists exception thrown");
            throw new CurrencyException(ErrorMessageEnum.PAIR_ALREADY_EXISTS);
        }
    }


    private Exchangerate getExchangeRateFromDTO(BigDecimal rate, String baseCode, String targetCode) {
        Exchangerate exchangeRateNew = new Exchangerate();
        exchangeRateNew.setRate(rate);
        exchangeRateNew.setBaseCurrency(getCurrencyFromCode(baseCode));
        exchangeRateNew.setTargetCurrency(getCurrencyFromCode(targetCode));
        return exchangeRateNew;
    }

    private Currency getCurrencyFromCode(String baseCode) {
        return currencyService.findByCode(baseCode);
    }

    @Override
    @Transactional
    public Exchangerate update(int id, ExchangeRateDto rateDto) {
        log.info("update exchange rateDto in database with ID: " + id);
        if (findById(id) == null) {
            log.error("not found exception thrown");
            throw new CurrencyException(ErrorMessageEnum.PAIR_NOT_FOUND);
        }
        String baseCode = rateDto.getBaseCode();
        String targetCode = rateDto.getTargetCode();
        BigDecimal rate = rateDto.getRate();
        if (!findByCodePairForAdd(baseCode, targetCode)) {
            Exchangerate exchangerate = getExchangeRateFromDTO(rate, baseCode, targetCode);
            return exchangeRateRepository.save(exchangerate);
        }
        log.error("already exists exception thrown");
        throw new CurrencyException( ErrorMessageEnum.PAIR_ALREADY_EXISTS);
    }

    public boolean findByCodePairForAdd(String baseCode, String targetCode) {
        log.info("findByCodePair exchange rate : {}, {}", baseCode, targetCode);
        return exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCode, targetCode).isEmpty();
    }

    @Override
    @Transactional
    public void delete(int id) {
        log.info("delete exchange rate from database : " + id);
        exchangeRateRepository.delete(findById(id));
    }
}
