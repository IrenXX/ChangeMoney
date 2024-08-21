package ru.kemova.currency_exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kemova.currency_exchange.model.Currency;
import ru.kemova.currency_exchange.services.CurrencyService;

import java.util.List;

@RestController
@RequestMapping("/currency-exchange")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/currencies")//findAll
    public List<Currency> findAll() {
        return currencyService.findAll();
    }

    @GetMapping("/{id}") //findById
    public Currency findById(@PathVariable Integer id) {
        return currencyService.findById(id);
    }

    @PostMapping("/currencies")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Currency currency) {
        currencyService.create(currency);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateProduct(@RequestBody Currency currency) {
        currencyService.update(currency);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    void delete(@PathVariable Integer id) {
        currencyService.delete(id);
    }
}