package ru.kemova.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.*;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.repository.CurrencyRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.kemova.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AllArgsConstructor
class CurrencyRepositoryTest {
    private CurrencyRepository repository;

//    @BeforeEach
//    void setUp() {
//        repository = new CurrencyRepository();
//    }

    @Test
    @Order(1)
    void findAll() {
        List<Currency> actual = repository.findAll();
        assertTrue(actual.containsAll(currencies));
//        assertThat(actual).hasSameElementsAs(TestData.currencies);
    }

    @Test
    @Order(2)
    void findByCode() {
        Currency actual = repository.findByCode(CODE).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
    }

    @Test
    @Order(3)
    void create() {
        Currency currency = repository.save(CURRENCY_TO_CREATE);
        assertThat(currency).usingRecursiveComparison()
                .isEqualTo(repository.findByCode(CURRENCY_TO_CREATE.getCode()).get());
    }

    @Test
    @Order(4)
    void update() {
        CURRENCY_TO_UPDATE.setCode(repository.findByCode(CURRENCY_TO_CREATE.getCode()).get().getCode());
        repository.save(CURRENCY_TO_UPDATE);
        assertThat(CURRENCY_TO_UPDATE).usingRecursiveComparison()
                .isEqualTo(repository.findByCode(CURRENCY_TO_UPDATE.getCode()).get());
    }

    @Test
    @Order(5)
    void delete() {
        repository.delete(repository.findByCode(CURRENCY_TO_UPDATE.getCode()).get());
        assertTrue(repository.findByCode(CURRENCY_TO_UPDATE.getCode()).isEmpty());
    }
}