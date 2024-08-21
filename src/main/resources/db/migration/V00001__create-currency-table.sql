CREATE TABLE IF NOT EXISTS currency (
      id SERIAL PRIMARY KEY,
      code VARCHAR NOT NULL ,
      full_name VARCHAR NOT NULL ,
      sign VARCHAR NOT NULL );

CREATE UNIQUE INDEX idx_code ON currency (code);

CREATE TABLE IF NOT EXISTS exchangeRate (
      id SERIAL PRIMARY KEY ,
      base_currency_id INTEGER NOT NULL ,
      target_currency_id INTEGER NOT NULL ,
      rate DECIMAL(6) NOT NULL ,
      FOREIGN KEY (base_currency_id) REFERENCES currency (id) ON DELETE CASCADE,
      FOREIGN KEY (target_currency_id) REFERENCES currency (id) ON DELETE CASCADE );

CREATE UNIQUE INDEX idx_base_target ON exchangeRate (base_currency_id, target_currency_id);

INSERT INTO currency (code, full_name, sign)
VALUES ('AUD', 'Australian dollar', 'A$'),
       ('USD', 'US dollar', '$'),
       ('RUB', 'Russian ruble', '₽'),
       ('EUR', 'Euro', '€');

INSERT INTO exchangeRate (base_currency_id, target_currency_id, rate)
VALUES (2, 3, 90.59), (2, 4, 0.89);
