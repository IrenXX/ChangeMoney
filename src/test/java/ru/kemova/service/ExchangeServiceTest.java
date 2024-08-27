package ru.kemova.service;

import org.junit.jupiter.api.*;
import ru.kemova.currencyexchange.services.ExchangeService;
import ru.kemova.currencyexchange.util.CurrencyException;

import static ru.kemova.TestData.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExchangeServiceTest {

    public ExchangeService service;

//    @BeforeEach
//    void setUp() {
//        service = new ExchangeService();
//    }

    @Test
    void exchangeStraight() {
        Assertions.assertEquals(service.exchange(EXCHANGE_FROM, EXCHANGE_TO, EXCHANGE_AMOUNT),
                STRAIGHT_RESULT);
    }

    @Test
    void exchangeReversed() {
        Assertions.assertEquals(service.exchange(EXCHANGE_TO, EXCHANGE_FROM, EXCHANGE_AMOUNT),
                REVERSED_RESULT);
    }

    @Test
    void exchangeUsdCrossRate() {
        Assertions.assertEquals(service.exchange(EXCHANGE_TO, EXCHANGE_CROSS_FROM, EXCHANGE_AMOUNT),
                USD_CROSS_RESULT);
    }

    @Test
    void exchangeRounding() {
        int expectedLength = String.valueOf(STRAIGHT_RESULT).length();
        int actualLength = String.valueOf(service.exchange(EXCHANGE_FROM, EXCHANGE_TO, EXCHANGE_AMOUNT)).length();
        Assertions.assertEquals(expectedLength, actualLength);
    }

    @Test
    void exchangePairInvalid() {
        Assertions.assertThrows(CurrencyException.class,
                () -> service.exchange(EXCHANGE_FROM, INVALID_CODE, EXCHANGE_AMOUNT));
    }

    @Test
    void exchangeSame() {
        Assertions.assertThrows(CurrencyException.class,
                () -> service.exchange(EXCHANGE_FROM, EXCHANGE_FROM, EXCHANGE_AMOUNT));
    }
}