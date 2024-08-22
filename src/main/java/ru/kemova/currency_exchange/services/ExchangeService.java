package ru.kemova.currency_exchange.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kemova.currency_exchange.dto.ExchangeRateDto;
import ru.kemova.currency_exchange.model.Currency;
import ru.kemova.currency_exchange.model.ExchangeRate;
import ru.kemova.currency_exchange.repository.CurrencyRepository;
import ru.kemova.currency_exchange.repository.ExchangeRateRepository;
import ru.kemova.currency_exchange.util.CurrencyException;

import java.math.BigDecimal;
import java.util.Objects;
//import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeService {
    private static final int ROUND_SCALE = 4;
    private final ExchangeRateServiceImpl exchangeService;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    //    public ExchangeDto getTo(String from, String to, String amount) {
//        ExchangeDto dto = new ExchangeDto();
//        dto.setBase(CURRENCY_SERVICE_IMPL.findByCode(from));
//        dto.setTarget(CURRENCY_SERVICE_IMPL.findByCode(to));
//        dto.setAmount(Double.parseDouble(amount));
//        dto.setRate(exchangeService.findByCodePair(from, to).getRate());
//        dto.setConvertedAmount(exchange(from, to, amount));
//        log.info("get transfer object");
//        return dto;
//    }

    private ExchangeRate getExchangeRateFromDTO(ExchangeRateDto rate) {
        ExchangeRate to = new ExchangeRate();
        to.setId(rate.getId());
        to.setBaseCurrency(currencyRepository.findByCode(rate.getBaseCode())
                .orElseThrow(() -> new CurrencyException("Currency should be not null")));
        to.setTargetCurrency(currencyRepository.findByCode(rate.getTargetCode())
                .orElseThrow(() -> new CurrencyException("Currency should be not null")));
        to.setRate(rate.getRate());
        log.info("get transfer object");
        return to;
    }

    @Transactional
    public double exchange(ExchangeRateDto exchangeRateDto, String amountString) {

        ExchangeRate exchangeRate = getExchangeRateFromDTO(exchangeRateDto);
        String from = exchangeRateDto.getBaseCode();
        Currency fromBase = exchangeRate.getBaseCurrency();
        String to = exchangeRateDto.getTargetCode();
        Currency toTarget = exchangeRate.getTargetCurrency();
        double rate = exchangeRateDto.getRate();
        double answer = 0.0;
        double amount = Double.parseDouble(amountString);
        log.debug("exchange from {} to {} amount {}", from, to, amount);

        if (from.isEmpty() || to.isEmpty() || from.equals(to)) {
            log.error("not found exception thrown");
            throw new CurrencyException("currency-pair not found");
        } else if (exchangeRateRepository
                .findExchangeRateByBaseCurrencyIdAndTargetCurrencyId(fromBase, toTarget)
                .isPresent()) {
            log.info("straight strategy from {} to {}", from, to);
            answer = roundDoubles(amount * rate);
        } else if (exchangeRateRepository
                .findExchangeRateByBaseCurrencyIdAndTargetCurrencyId(fromBase, toTarget)
                .isPresent()) {
            log.info("reversed strategy to {} from {}", to, from);
            rate = 1 / rate;
            answer = roundDoubles(amount * rate);
        } else {
            Currency usd = currencyRepository.findByCode("USD").orElseThrow(()->
                    new CurrencyException("for cross-rate need USD"));
            if ((exchangeRateRepository.
                    findExchangeRateByBaseCurrencyIdAndTargetCurrencyId(usd, fromBase).isPresent())
                    && (exchangeRateRepository
                    .findExchangeRateByBaseCurrencyIdAndTargetCurrencyId(usd, toTarget).isPresent())) {
                String base = "USD";
                ExchangeRate usdFromRate = exchangeService.findByCodePair(base, from);
                ExchangeRate usdToRate = exchangeService.findByCodePair(base, to);
                rate = 1 / (usdFromRate.getRate() / usdToRate.getRate());
                answer = roundDoubles(amount * rate);
                log.info("USD cross-rate strategy USD - {}, USD - {}", from, to);
            }
        }
        return answer;
    }

    private static double roundDoubles(double number) {
        BigDecimal around = new BigDecimal(number);
        around = around.setScale(ROUND_SCALE);
        return around.doubleValue();
    }
}
