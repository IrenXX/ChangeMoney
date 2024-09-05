package ru.kemova.currencyexchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.services.ExchangeRateService;
import ru.kemova.currencyexchange.services.impl.ExchangeServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/exchangeRate")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;
    private final ExchangeServiceImpl exchangeService;
//    @PostConstruct
    @GetMapping("/findAll")
    public List<Exchangerate> findAll() {
        return exchangeRateService.findAll();
    }

    @GetMapping
    public double exchangeFromTo(@RequestParam String from, @RequestParam String to,
                                 @RequestParam int amount) {
        return exchangeService.exchange(from, to, amount);
    }

    @GetMapping("/{code}") //findByCodePair
    public double findByCodePair(@PathVariable String code) {
        String baseCurrency, targetCurrency;
        baseCurrency = code.substring(0, 3);
        targetCurrency = code.substring(3);
        return exchangeRateService.findByCodePair(baseCurrency, targetCurrency).getRate();
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