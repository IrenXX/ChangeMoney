package ru.kemova.currencyexchange.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.ExchangeDto;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.repository.CurrencyRepository;
import ru.kemova.currencyexchange.repository.ExchangeRateRepository;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private static final int ROUND_SCALE = 4;
    private final ExchangeRateServiceImpl exchangeService;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    @Transactional
    public double exchange(String from, String to, int amount) {

        Exchangerate codePair = exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(from, to)
                .orElseThrow(() ->
                {
                    log.error("not found exception thrown");
                    throw new CurrencyException("currency-pair not found");
                });

        double rate = codePair.getRate();
        double answer = 0.0;
        log.debug("exchange from {} to {} amount {}", from, to, amount);


        if (!from.equals(to)) {
            log.info("straight strategy from {} to {}", from, to);
            answer = roundDoubles(amount * rate);
            if (exchangeRateRepository
                    .findByBaseCurrencyCodeAndTargetCurrencyCode(to, from)
                    .isPresent()) {
                log.info("reversed strategy to {} from {}", to, from);
                rate = 1 / rate;
                answer = roundDoubles(amount * rate);
            } else {
                String base = "USD";
                Exchangerate usdFromRate = exchangeRateRepository.
                        findByBaseCurrencyCodeAndTargetCurrencyCode(base, from)
                        .orElseThrow(() -> new CurrencyException("currency-pair not found"));
                Exchangerate usdToRate = exchangeRateRepository.
                        findByBaseCurrencyCodeAndTargetCurrencyCode(base, to)
                        .orElseThrow(() -> new CurrencyException("currency-pair not found"));
                    rate = 1 / (usdFromRate.getRate() / usdToRate.getRate());
                    answer = roundDoubles(amount * rate);
                    log.info("USD cross-rate strategy USD - {}, USD - {}", from, to);
            }
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

    private static double roundDoubles(double number) {
        BigDecimal around = new BigDecimal(number);
        return around.setScale(ROUND_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
