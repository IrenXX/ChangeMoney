package ru.kemova.currencyexchange.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.ExchangeDto;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.repository.ExchangeRateRepository;
import ru.kemova.currencyexchange.services.ExchangeService;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private static final int ROUND_SCALE = 4;
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    @Transactional
    public BigDecimal exchange(String from, String to, BigDecimal amount) {

        Optional<Exchangerate> codePair = exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(from, to);
        Optional<Exchangerate> codePairRevers = exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(to, from);

        BigDecimal rate;
        BigDecimal a = BigDecimal.valueOf(1);
        BigDecimal answer;
        log.info("exchange from {} to {} amount {}", from, to, amount);

        if (codePair.isPresent()) {
            rate = codePair.get().getRate();
            answer = rate.multiply(amount);
            log.info("straight strategy from {} to {}", from, to);
        } else if (codePairRevers.isPresent()) {
            rate = codePairRevers.get().getRate();
            rate = a.divide(rate, ROUND_SCALE, BigDecimal.ROUND_UP);
            answer = rate.multiply(amount);
            log.info("reversed strategy to {} from {}", to, from);
        } else {
            String base = "USD";
            Exchangerate usdFromRate = exchangeRateRepository.
                    findByBaseCurrencyCodeAndTargetCurrencyCode(base, from)
                    .orElseThrow(() -> new CurrencyException("currency-pair not found"));
            Exchangerate usdToRate = exchangeRateRepository.
                    findByBaseCurrencyCodeAndTargetCurrencyCode(base, to)
                    .orElseThrow(() -> new CurrencyException("currency-pair not found"));
            rate = a.divide(usdFromRate.getRate(), ROUND_SCALE, BigDecimal.ROUND_UP)
                    .divide(usdToRate.getRate(), ROUND_SCALE, BigDecimal.ROUND_UP);
            answer = rate.multiply(amount).setScale(ROUND_SCALE);
            log.info("USD cross-rate strategy USD - {}, USD - {}", from, to);
        }
        return answer;
}

    private Exchangerate getExchangeFromDTO(ExchangeDto exchangeDto) {
        Exchangerate to = exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(exchangeDto.getFrom(),
                        exchangeDto.getTo())
                .orElseThrow(() -> new CurrencyException("Currency (base and target code) should be not null"));
        log.info("get transfer object");
        return to;
    }
}
