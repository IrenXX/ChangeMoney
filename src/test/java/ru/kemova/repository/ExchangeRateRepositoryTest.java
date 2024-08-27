package ru.kemova.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.repository.ExchangeRateRepository;
import ru.kemova.currencyexchange.services.ExchangeRateService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static ru.kemova.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
class ExchangeRateRepositoryTest {

    private ExchangeRateRepository repository;
    private ExchangeRateService service;

//    @BeforeEach
//    void setUp() {
//        repository = new ExchangeRateJdbcRepository();
//    }

    @Test
    @Order(1)
    void findAll() {
        List<Exchangerate> actual = repository.findAll();
        Assertions.assertTrue(actual.containsAll(rates));
//        assertThat(actual).hasSameElementsAs(TestData.rates);
    }

    @Test
    @Order(2)
    void findByCodePair() {
        Exchangerate actual = repository.findByBaseCurrencyCodeAndTargetCurrencyCode(BASE, TARGET).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(rate1);
    }
//
//    @Test
//    @Order(3)
//    void getById() {
//        ExchangeRate actual = repository.getById(RATE_ID).orElse(null);
//        assertThat(actual).usingRecursiveComparison().isEqualTo(rate1);
//    }

    @Test
    @Order(4)
    void create() {
        Exchangerate rate = repository.save(rate1);
        assertThat(rate).usingRecursiveComparison()
                .isEqualTo(repository.findByBaseCurrencyCodeAndTargetCurrencyCode(rate1.getBaseCurrency().getCode(),
                        rate1.getTargetCurrency().getCode()).get());
    }

    @Test
    @Order(5)
    void update() {
        rate2.setId(repository.findByBaseCurrencyCodeAndTargetCurrencyCode(rate2.getBaseCurrency().getCode(),
                rate2.getTargetCurrency().getCode()).get().getId());
        repository.save(rate2);
        assertThat(rate2).usingRecursiveComparison()
                .isEqualTo(repository.findById(rate2.getId()).get());
    }

    @Test
    @Order(6)
    void delete() {
        Exchangerate exchangerate = repository.findByBaseCurrencyCodeAndTargetCurrencyCode(rate2.getBaseCurrency().getCode(),
                rate2.getTargetCurrency().getCode()).get();
        repository.delete(exchangerate);
        assertTrue(repository.findById(exchangerate.getId()).isPresent(),"Delete");
    }
}