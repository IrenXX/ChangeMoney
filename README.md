# currency-exchange-service
Currency exchange service : REST API, Json, Flyway, Spring Data, Lombok, Jackson, Slf4J, JUnit, PostgreSQL

# Postman documentation
https://documenter.getpostman.com/view/26533447/2s946o2oDU

### Application is available at <a href="http://localhost:8080/currency-exchange/"> currency-exchange <a/>

# Техническое задание
REST API для описания валют и обменных курсов. Позволяет просматривать и редактировать списки валют и обменных курсов, совершать расчёт конвертации произвольных сумм из одной валюты в другую.

Веб-интерфейс для проекта не подразумевается.

# REST API
#### - GET /currencies
Получение списка валют.

#### - GET /currency/EUR
Получение конкретной валюты.

#### - POST /currencies
Добавление новой валюты в базу. Данные передаются в теле запроса в в формате JSon. Ключи - - name, code, sign. 

#### - GET /exchangeRates
Получение списка всех обменных курсов. 

#### - GET /exchangeRate/USDRUB
Получение конкретного обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. 

#### - POST /exchangeRates
Добавление нового обменного курса в базу. Данные передаются в теле запроса в формате JSon. Ключи - baseCurrencyCode, targetCurrencyCode, rate.

#### - PUT /exchangeRate/USDRUB
Обновление существующего в базе обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. Данные передаются в теле запроса в формате JSon. Единственное поле формы - rate.

#### - GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT
Расчёт перевода определённого количества средств из одной валюты в другую. Пример запроса - GET /exchange?from=USD&to=AUD&amount=10.

#### Получение курса для обмена может пройти по одному из трёх сценариев. Допустим, совершаем перевод из валюты A в валюту B:

В таблице ExchangeRates существует валютная пара AB - берём её курс
В таблице ExchangeRates существует валютная пара BC - берем её курс, считаем обратный курс, чтобы получить AB
В таблице ExchangeRates существуют валютные пары USD-A и USD-B - вычисляем из этих курсов курс AB
Остальные возможные сценарии, для упрощения, опустим.

Для всех запросов, в случае ошибки, ответ может выглядеть так:

{
"message": "Валюта не найдена"
}
Значение message зависит от того, какая именно ошибка произошла.

### <a href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/">Более подробное техническое задание<a/>
