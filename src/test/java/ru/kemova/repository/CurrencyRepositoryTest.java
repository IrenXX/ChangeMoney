package ru.kemova.repository;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kemova.currencyexchange.CurrencyExchangeApplication;
import ru.kemova.currencyexchange.model.Currency;
import ru.kemova.currencyexchange.repository.CurrencyRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.kemova.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
class CurrencyRepositoryTest {
    @MockBean
    private CurrencyRepository repository;
//    @Autowired
//    private TestEntityManager entityManager;

//    @BeforeEach
//    void setUp() {
//        repository = new CurrencyRepository() {
//            @Override
//            public <S extends Currency, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//                return null;
//            }
//
//            @Override
//            public List<Currency> findAll() {
//                return null;
//            }
//        };
//    }

    @Test
    @Order(1)
    public void findAllTest() {
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