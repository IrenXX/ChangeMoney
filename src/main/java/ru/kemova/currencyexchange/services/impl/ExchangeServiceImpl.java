package ru.kemova.currencyexchange.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currencyexchange.dto.ExchangeDto;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.repository.CurrencyRepository;
import ru.kemova.currencyexchange.repository.ExchangeRateRepository;
import ru.kemova.currencyexchange.services.ExchangeRateService;
import ru.kemova.currencyexchange.services.ExchangeService;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private static final int ROUND_SCALE = 4;
    private final ExchangeRateService exchangeService;

    @Override
    @Transactional
    public double exchange(String from, String to, int amount) {

        Exchangerate codePair = exchangeService.findByCodePair(from, to);

        double rate = codePair.getRate();
        double answer = 0.0;
        log.debug("exchange from {} to {} amount {}", from, to, amount);


        if (!from.equals(to)) {
            log.info("straight strategy from {} to {}", from, to);
            answer = roundDoubles(amount * rate);
            if (codePair!=null) {
                log.info("reversed strategy to {} from {}", to, from);
                rate = 1 / rate;
                answer = roundDoubles(amount * rate);
            } else {
                String base = "USD";
                Exchangerate usdFromRate = exchangeService.
                        findByCodePair(base, from);
                Exchangerate usdToRate = exchangeService.
                        findByCodePair(base, to);
                    rate = 1 / (usdFromRate.getRate() / usdToRate.getRate());
                    answer = roundDoubles(amount * rate);
                    log.info("USD cross-rate strategy USD - {}, USD - {}", from, to);
            }
        }
        return answer;
    }

    private Exchangerate getExchangeFromDTO(ExchangeDto exchangeDto) {
        Exchangerate to = exchangeService.findByCodePair(exchangeDto.getFrom(),
                        exchangeDto.getTo());
        log.info("get transfer object");
        return to;
    }

    private static double roundDoubles(double number) {
        BigDecimal around = new BigDecimal(number);
        return around.setScale(ROUND_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
