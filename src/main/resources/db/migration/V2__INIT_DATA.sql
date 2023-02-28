INSERT INTO exchange_user(user_id, username, role, email) VALUES('f24de643-ace3-4224-8534-681d6c329aca',
                                                                 'admin', 'ADMIN', 'admin@rule.ru');
INSERT INTO currency(name) VALUES('RUB');
INSERT INTO currency(name) VALUES('TON');
INSERT INTO currency(name) VALUES('BTC');

INSERT INTO exchange_rate(currency_id, another_currency_currency_id, exchange_rate) VALUES(1, 2, 0.0055);
INSERT INTO exchange_rate(currency_id, another_currency_currency_id, exchange_rate) VALUES(2, 1, 180);
INSERT INTO exchange_rate(currency_id, another_currency_currency_id, exchange_rate) VALUES(1, 3, 0.000000531);
INSERT INTO exchange_rate(currency_id, another_currency_currency_id, exchange_rate) VALUES(3, 1, 1882057);
INSERT INTO exchange_rate(currency_id, another_currency_currency_id, exchange_rate) VALUES(2, 3, 0.0000957);
INSERT INTO exchange_rate(currency_id, another_currency_currency_id, exchange_rate) VALUES(3, 2, 10455.9);