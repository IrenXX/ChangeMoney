CREATE TABLE IF NOT EXISTS currency (
        code VARCHAR PRIMARY KEY ,
        full_name VARCHAR NOT NULL ,
        sign VARCHAR NOT NULL );

CREATE UNIQUE INDEX idx_code ON currency (code);

CREATE TABLE IF NOT EXISTS exchangerate (
        id SERIAL PRIMARY KEY ,
        base_currency_code VARCHAR NOT NULL ,
        target_currency_code VARCHAR NOT NULL ,
        rate DECIMAL(6, 4) NOT NULL ,
        FOREIGN KEY (base_currency_code) REFERENCES currency (code) ON DELETE CASCADE,
        FOREIGN KEY (target_currency_code) REFERENCES currency (code) ON DELETE CASCADE );

CREATE UNIQUE INDEX idx_base_target ON exchangerate (base_currency_code, target_currency_code);

INSERT INTO currency (code, full_name, sign)
VALUES ('AUD', 'Australian dollar', 'A$'),
       ('USD', 'US dollar', '$'),
       ('RUB', 'Russian ruble', '₽'),
       ('EUR', 'Euro', '€');

INSERT INTO exchangeRate (base_currency_code, target_currency_code, rate)
VALUES ('USD', 'RUB', 90.59), ('USD', 'EUR', 0.89), ('RUB', 'EUR', 0.0099);
