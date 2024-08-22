package ru.kemova.currency_exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kemova.currency_exchange.dto.ExchangeRateDto;
import ru.kemova.currency_exchange.model.ExchangeRate;
import ru.kemova.currency_exchange.services.ExchangeRateService;
import ru.kemova.currency_exchange.services.ExchangeService;

import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;
    private final ExchangeService exchangeService;

    @GetMapping("/exchangeRates")//findAll
    public List<ExchangeRate> findAll() {
        return exchangeRateService.findAll();
    }

    @GetMapping("/exchange/{amount}")
    public double exchangeFromTo(@RequestBody ExchangeRateDto exchangeRateDto, @PathVariable String amount) {
        return exchangeService.exchange(exchangeRateDto, amount);
    }

    @GetMapping("/exchangeRate/{code}") //findByCodePair
    public ExchangeRate findByCodePair(@PathVariable String code) {
        String baseCurrency, targetCurrency;
        baseCurrency = code.substring(0, 3);
        targetCurrency = code.substring(3);
        return exchangeRateService.findByCodePair(baseCurrency, targetCurrency);
    }

    @PostMapping("/exchangeRate")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ExchangeRate exchangeRate) {
        exchangeRateService.create(exchangeRate);
    }

    @PutMapping("/exchangeRate")//TODO
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@RequestBody ExchangeRate exchangeRate) {
        exchangeRateService.update(exchangeRate);
    }

    @DeleteMapping("/exchangeRate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        exchangeRateService.delete(id);
    }
}