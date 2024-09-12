package ru.kemova.currencyexchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.dto.ExchangeResponseDto;
import ru.kemova.currencyexchange.model.Exchangerate;
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
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Exchangerate> findAll() {
        return exchangeRateService.findAll();
    }

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal findByCodePair(@PathVariable String code) {
        String baseCurrency, targetCurrency;
        baseCurrency = code.substring(0, 3).toUpperCase();
        targetCurrency = code.substring(3).toUpperCase();
        return exchangeRateService.findByCodePair(baseCurrency, targetCurrency).getRate();
    }

    @GetMapping("/change")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeResponseDto exchangeFromTo(@RequestParam String from, @RequestParam String to,
                                              @RequestParam BigDecimal amount) {
        return exchangeService.exchange(from.toUpperCase(), to.toUpperCase(), amount);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Exchangerate create(@RequestBody ExchangeRateDto exchangeRateDto) {
        return exchangeRateService.create(exchangeRateDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable int id, @RequestBody ExchangeRateDto exchangeRateDto) {
        exchangeRateService.update(id, exchangeRateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id) {
        exchangeRateService.delete(id);
    }
}