package ru.kemova.currencyexchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kemova.currencyexchange.dto.CurrencyDto;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.services.CurrencyService;

import java.util.List;

@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public List<Currency> findAll() {
        return currencyService.findAll();
    }

    @GetMapping("/{code}")
    public Currency findByCode(@PathVariable String code) {
        return currencyService.findByCode(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Currency currency) {
        currencyService.create(currency);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Currency currency) {
        currencyService.update(currency);
    }

    @DeleteMapping("/delete/{code}")
    @ResponseStatus(HttpStatus.GONE)
    public void delete(@PathVariable String code) {
        currencyService.delete(code);
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    private StockErrorResponse handleException(CurrencyException exception) {
//        return new StockErrorResponse(exception.getMessage());
//    }
}