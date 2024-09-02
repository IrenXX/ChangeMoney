package ru.kemova.currencyexchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.services.ExchangeRateService;
import ru.kemova.currencyexchange.services.ExchangeService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/exchangeRate")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;
    private final ExchangeService exchangeService;
//    @PostConstruct
    @GetMapping
    public List<ExchangeRateDto> findAll() {
        return exchangeRateService.findAll();
    }

    @GetMapping("/change")
    public BigDecimal exchangeFromTo(@RequestParam String from, @RequestParam String to,
                                 @RequestParam BigDecimal amount) {
        return exchangeService.exchange(from, to, amount);
    }

    @GetMapping("/{code}") //findByCodePair
    public BigDecimal findByCodePair(@PathVariable String code) {
        String baseCurrency, targetCurrency;
        baseCurrency = code.substring(0, 3);
        targetCurrency = code.substring(3);
        return exchangeRateService.findByCodePair(baseCurrency, targetCurrency);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ExchangeRateDto rateDto) {
        exchangeRateService.create(rateDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        exchangeRateService.delete(id);
    }
}