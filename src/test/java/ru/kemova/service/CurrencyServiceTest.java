package ru.kemova.service;

import org.junit.jupiter.api.*;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.services.CurrencyService;
import ru.kemova.currencyexchange.util.CurrencyException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.kemova.TestData.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CurrencyServiceTest {

    private CurrencyService service;

//    @BeforeEach
//    void setUp() {
//        service = new CurrencyService();
//    }

    @Test
    @Order(1)
    void findAll() {
        List<Currency> actual = service.findAll();
        Assertions.assertTrue(actual.containsAll(currencies));
//        assertThat(actual).containsExactlyInAnyOrderElementsOf(TestData.currencies);
    }

    @Test
    @Order(2)
    void findByCode() {
        Currency actual = service.findByCode(CODE);
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
    }

    @Test
    @Order(3)
    void getByNotExistedCode() {
        Assertions.assertThrows(CurrencyException.class, () -> service.findByCode(INVALID_CODE));
    }

//    @Test
//    @Order(4)
//    void getById() {
//        Currency actual = service.fi(CURRENCY_ID);
//        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
//    }

    @Test
    @Order(6)
    void create() {
        Currency currency = service.create(CURRENCY_TO_CREATE);
        assertThat(currency).usingRecursiveComparison()
                .isEqualTo(service.findByCode(CURRENCY_TO_CREATE.getCode()));
    }

    @Test
    @Order(7)
    void createExisted() {
        Assertions.assertThrows(CurrencyException.class, () -> service.create(currency1));
    }

    @Test
    @Order(8)
    void update() {
        CURRENCY_TO_UPDATE.setCode(service.findByCode(CURRENCY_TO_CREATE.getCode()).getCode());
        service.update(CURRENCY_TO_UPDATE);
        assertThat(CURRENCY_TO_UPDATE).usingRecursiveComparison()
                .isEqualTo(service.findByCode(CURRENCY_TO_UPDATE.getCode()));
    }

    @Test
    @Order(9)
    void updateWithInvalidId() {
        CURRENCY_TO_UPDATE.setCode(INVALID_CODE);
        Assertions.assertThrows(NullPointerException.class, () -> service.update(CURRENCY_TO_UPDATE));
    }

    @Test
    @Order(10)
    void updateWithNullId() {
        Assertions.assertThrows(NullPointerException.class, () -> service.update(CURRENCY_TO_UPDATE));
    }

    @Test
    @Order(11)
    void delete() {
        Currency byCode = service.findByCode(CURRENCY_TO_UPDATE.getCode());
        service.delete(byCode.getCode());
        Assertions.assertThrows(NullPointerException.class, () -> service.findByCode(byCode.getCode()));
    }

    @Test
    @Order(12)
    void deleteWithInvalidId() {
        Assertions.assertThrows(NullPointerException.class, () -> service.delete(INVALID_CODE));
    }
}