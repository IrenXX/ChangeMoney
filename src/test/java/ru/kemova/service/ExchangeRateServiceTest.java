package ru.kemova.service;

import org.junit.jupiter.api.*;
import ru.kemova.currencyexchange.dto.ExchangeRateDto;
import ru.kemova.currencyexchange.model.Exchangerate;
import ru.kemova.currencyexchange.repository.ExchangeRateRepository;
import ru.kemova.currencyexchange.services.ExchangeRateService;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.kemova.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExchangeRateServiceTest {

    private ExchangeRateService service;
    private ExchangeRateRepository repository;

//    @BeforeEach
//    void setUp() {
//        service = new ExchangeRateService();
//    }

    @Test
    @Order(1)
    void findAll() {
        List<Exchangerate> actual = service.findAll();
        Assertions.assertTrue(actual.containsAll(rates));
//        assertThat(actual).containsExactlyInAnyOrderElementsOf(TestData.rates);
    }

    @Test
    @Order(2)
    void findByCodePair() {
        Exchangerate actual = repository.findByBaseCurrencyCodeAndTargetCurrencyCode(BASE, TARGET).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(rate1);
    }

    @Test
    @Order(3)
    void findByNotExistedPair() {
        Assertions.assertThrows(CurrencyException.class,
                () -> service.findByCodePair(BASE, INVALID_CODE));
    }

//    @Test
//    @Order(4)
//    void getById() {
//        Exchangerate actual = service.f(RATE_ID);
//        assertThat(actual).usingRecursiveComparison().isEqualTo(rate1);
//    }

    @Test
    @Order(5)
    void findByNotExistedId() {
        Assertions.assertThrows(CurrencyException.class, () -> repository.findById(INVALID_ID));
    }

    @Test
    @Order(6)
    void create() {
        Exchangerate rate = service.create(RATE_TO_CREATE);
        assertThat(rate).usingRecursiveComparison()
                .isEqualTo(service.findByCodePair(RATE_TO_CREATE.getBaseCode(),
                        RATE_TO_CREATE.getTargetCode()));
    }

    @Test
    @Order(8)
    void createExisted() {
        Assertions.assertThrows(CurrencyException.class, () -> service.create(rateDto));
    }

    @Test
    @Order(11)
    void delete() {
        int id = repository.findByBaseCurrencyCodeAndTargetCurrencyCode(RATE_TO_UPDATE.getBaseCode(),
                RATE_TO_UPDATE.getTargetCode()).get().getId();
        service.delete(id);
        Assertions.assertThrows(CurrencyException.class, () -> repository.findById(id));
    }

    @Test
    @Order(12)
    void deleteWithInvalidId() {
        Assertions.assertThrows(CurrencyException.class, () -> service.delete(INVALID_ID));
    }
}