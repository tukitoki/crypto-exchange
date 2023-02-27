CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS exchange_user(
    user_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    email VARCHAR(200) NOT NULL,
    username VARCHAR(50) NOT NULL,
    role VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS transaction(
    operation_id SERIAL NOT NULL PRIMARY KEY,
    secret_key VARCHAR NOT NULL,
    type VARCHAR(20),
    date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS currency(
    currency_id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(3)
);

CREATE TABLE IF NOT EXISTS exchange_rate(
    exchange_rate_id SERIAL NOT NULL PRIMARY KEY,
    currency_id INT NOT NULL REFERENCES currency(currency_id) ON DELETE CASCADE,
    another_currency_currency_id INT NOT NULL REFERENCES currency(currency_id) ON DELETE CASCADE,
    exchange_rate DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS user_currency_amount(
    user_id uuid NOT NULL REFERENCES exchange_user (user_id) ON DELETE CASCADE,
    currency_id INT NOT NULL REFERENCES currency (currency_id) ON DELETE CASCADE,
    amount DOUBLE PRECISION NOT NULL,
    PRIMARY KEY(user_id, currency_id)
);