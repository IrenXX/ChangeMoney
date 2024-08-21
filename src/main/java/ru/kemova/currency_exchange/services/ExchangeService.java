package ru.kemova.currency_exchange.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kemova.currency_exchange.model.ExchangeRate;
import ru.kemova.currency_exchange.repository.CurrencyRepository;
import ru.kemova.currency_exchange.repository.ExchangeRateRepository;
import ru.kemova.currency_exchange.util.CurrencyException;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class ExchangeService {
    private static final int ROUND_SCALE = 4;
    private final ExchangeRateService exchangeService;
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyServiceImpl CURRENCY_SERVICE_IMPL;
    private final CurrencyRepository currencyRepository;

    private static double roundDoubles(double number) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(ROUND_SCALE);
        return bd.doubleValue();
    }

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
//
//    public static ExchangeRateDto getTo(ExchangeRate rate) {
//        ExchangeRateDto to = new ExchangeRateDto();
//        to.setId(rate.getId());
//        to.setBase(currencyRepo.getByCode(rate.getBaseCode()).get());
//        to.setTarget(currencyRepo.getByCode(rate.getTargetCode()).get());
//        to.setRate(rate.getRate());
//        return to;
//    }

    public double exchange(String from, String to, String amountString) {
        double answer = 0.0;
        double amount = Double.parseDouble(amountString);
        log.debug("exchange from {} to {} amount {}", from, to, amount);

        if (currencyRepository.findByCode(from).isEmpty()
                || currencyRepository.findByCode(to).isEmpty()
                || from.equals(to)) {
            log.error("not found exception thrown");
            throw new CurrencyException("currency-pair not found");
        } else if (exchangeRateRepository.findExchangeRateByBaseCodeAndTargetCode(from, to).isPresent()) {
            log.info("straight strategy from {} to {}", from, to);

            ExchangeRate changePair = exchangeService.findByCodePair(from, to);
            double rate = changePair.getRate();
            answer = roundDoubles(amount * rate);
        } else if (exchangeRateRepository.findExchangeRateByBaseCodeAndTargetCode(to, from).isPresent()) {
            log.info("reversed strategy to {} from {}", to, from);

            ExchangeRate changePair = exchangeService.findByCodePair(to, from);
            double rate = 1 / changePair.getRate();
            answer = roundDoubles(amount * rate);
        } else if ((exchangeRateRepository.findExchangeRateByBaseCodeAndTargetCode("USD", from).isPresent()) &&
                (exchangeRateRepository.findExchangeRateByBaseCodeAndTargetCode("USD", to).isPresent())) {
            log.info("USD cross-rate strategy USD - {}, USD - {}", from, to);
            String base = "USD";
            ExchangeRate usdFromRate = exchangeService.findByCodePair(base, from);
            ExchangeRate usdToRate = exchangeService.findByCodePair(base, to);
            double rate = 1 / (usdFromRate.getRate() / usdToRate.getRate());
            answer = roundDoubles(amount * rate);
        }
        return answer;
    }
}
