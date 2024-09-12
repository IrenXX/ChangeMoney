package ru.kemova.currencyexchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kemova.currencyexchange.dto.CurrencyDtoForUpd;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.services.CurrencyService;

import java.util.List;

@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Currency> findAll() {
        return currencyService.findAll();
    }

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Currency findByCode(@PathVariable String code) {
        return currencyService.findByCode(code.toUpperCase());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Currency currency) {
        currencyService.create(currency);
    }

    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String code, @RequestBody CurrencyDtoForUpd currency) {
        currencyService.update(code.toUpperCase(), currency);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.GONE)
    public void delete(@PathVariable String code) {
        currencyService.delete(code.toUpperCase());
    }
}