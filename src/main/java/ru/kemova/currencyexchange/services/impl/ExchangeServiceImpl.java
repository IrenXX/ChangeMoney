package ru.kemova.currencyexchange.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.ExchangeResponseDto;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.services.ExchangeRateService;
import ru.kemova.currencyexchange.services.ExchangeService;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private static final int ROUND_SCALE = 4;
    private final ExchangeRateService exchangeRateService;

    @Override
    @Transactional
    public ExchangeResponseDto exchange(String from, String to, BigDecimal amount) {

        Optional<Exchangerate> codePair = exchangeRateService.findByCodePairForExchange(from, to);
        Optional<Exchangerate> codePairRevers = exchangeRateService.findByCodePairForExchange(to, from);
        BigDecimal rate;
        BigDecimal a = BigDecimal.valueOf(1);
        BigDecimal convertedAmount;

        if (codePair.isPresent()) {
            log.info("exchange from {} to {} amount {}", from, to, amount);
            rate = codePair.get().getRate();
            convertedAmount = rate.multiply(amount);
            Currency baseCurrency = codePair.get().getBaseCurrency();
            Currency targetCurrency = codePair.get().getTargetCurrency();
            return createExchangeResponseDto(amount, rate, convertedAmount, baseCurrency, targetCurrency);

        } else if (codePairRevers.isPresent()) {
            log.info("reversed strategy to {} from {}", to, from);
            rate = codePairRevers.get().getRate();
            rate = a.divide(rate, ROUND_SCALE, BigDecimal.ROUND_UP);
            convertedAmount = rate.multiply(amount).setScale(ROUND_SCALE, BigDecimal.ROUND_HALF_UP);
            Currency baseCurrency = codePairRevers.get().getTargetCurrency();
            Currency targetCurrency = codePairRevers.get().getBaseCurrency();
            return createExchangeResponseDto(amount, rate, convertedAmount, baseCurrency, targetCurrency);

        }
        String base = "USD";
        Exchangerate usdFromRate = exchangeRateService.findByCodePair(base, from);
        Exchangerate usdToRate = exchangeRateService.findByCodePair(base, to);

        rate = usdToRate.getRate().divide(usdFromRate.getRate(), ROUND_SCALE, BigDecimal.ROUND_UP);
        convertedAmount = rate.multiply(amount).setScale(ROUND_SCALE, BigDecimal.ROUND_UP);
        Currency baseCurrency = usdFromRate.getTargetCurrency();
        Currency targetCurrency = usdToRate.getTargetCurrency();
        log.info("USD cross-rate strategy USD - {}, USD - {}", from, to);
        return createExchangeResponseDto(amount, rate, convertedAmount, baseCurrency, targetCurrency);
    }

    private ExchangeResponseDto createExchangeResponseDto(BigDecimal amount, BigDecimal rate, BigDecimal converterAmount,
                                                          Currency base, Currency target) {
        ExchangeResponseDto responseDto = new ExchangeResponseDto();
        responseDto.setAmount(amount);
        responseDto.setRate(rate);
        responseDto.setConvertedAmount(converterAmount);
        responseDto.setBaseCurrency(base);
        responseDto.setTargetCurrency(target);
        log.info("get transfer -> Exchange Response DTO");
        return responseDto;
    }
}
