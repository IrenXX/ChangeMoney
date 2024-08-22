package ru.kemova.currency_exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kemova.currency_exchange.model.Currency;
import ru.kemova.currency_exchange.services.CurrencyService;

import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/currencies")//findAll
    public List<Currency> findAll() {
        return currencyService.findAll();
    }

    @GetMapping("/currency/{code}") //findByCode
    public Currency findByCode(@PathVariable String code) {
        return currencyService.findByCode(code);
    }

    @PostMapping("/currency")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Currency currency) {
        currencyService.create(currency);
    }

    @PutMapping("/currency")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@RequestBody Currency currency) {
        currencyService.update(currency);
    }

    @DeleteMapping("/currency/{id}")
    @ResponseStatus(HttpStatus.GONE)
    public void delete(@PathVariable Integer id) {
        currencyService.delete(id);
    }
}