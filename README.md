# Crypto-exchange
Тестовый проект релэкс. Реализован RESTfull API service крипто-биржа.

## Использованные технологии:
- ### Java 17
- ### Maven
- ### Spring-boot
- ### PostgreSQL
- ### Flyway
- ### Lombok
- ### Swagger
- ### Slf4j

## Реализованные требования
- #### По умолчанию реализован полный API для USER и ADMIN (примеры запросов ниже)
### Дополнительные требования:
- #### Подключить базу данных Sqlite или PostgreSQL для хранения данных о балансе пользовательских кошельков и истории операций
- #### Сервис по запросу может возвращать данные в json ИЛИ xml
- #### Формат может быть изменен добавлением header путем добавления accept:application/json или accept:application/xml к HTTP-запросу
- #### Подключить и настроить swagger

## Запуск
Для запуска приложения необходимо иметь предустановленный PostreSQL
на компьтере. Порядок:
- #### Создание базы данных PostgreSQL
- #### Изменения параметров в application.properties, а именно названия созданной бд, имя пользователя и его пароль.

## Есть данные по умолчанию:
- ### ADMIN с secret_key f24de643-ace3-4224-8534-681d6c329aca
- ### 3 базовые валюты: RUB, TON, BTC с курсами относительно друг друга.

## USER API:
- #### Регистрация пользователя. POST запрос. URI **http://localhost:8080/api/user**
##### Примеры запросов:
    REQUEST:
    {
        "username": "tukitoki",
        "email": "tukitoki@mail.ru"
    }
    RESPONSE(HttpStatus.CREATED):
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59"
    }

    REQUEST:
    {
        "username": "fff2",
        "email": "tukitoki@mail.ru"
    }
    RESPONSE(HttpStatus.BAD_REQUEST): Email already present

    REQUEST:
    {
        "username": "tukitoki",
        "email": "sggad@mail.ru"
    }
    RESPONSE(HttpStatus.BAD_REQUEST): Username already present

    REQUEST: 
    {
        "username": "fff"
    }
    RESPONSE(HttpStatus.BAD_REQUEST): Enter your email

    REQUEST:
    {
        "email": "sgghad@mail.ru"
    } 
    RESPONSE(HttpStatus.BAD_REQUEST): Enter your username

- #### Пополнение баланса. POST запрос. URI **http://localhost:8080/api/balance/replenishment**
##### Примеры запросов:

    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency": "RUB",
        "amount": 500
    }
    RESPONSE(HttpStatus.OK):
    {
        "currency": "RUB",
        "amount": 500.0
    }


    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency": "RUB"
    }
    RESPONSE(HttpStatus.BAD_REQUEST): Amount should not be empty

    REQUEST:
    {
        "secret_key": "89059968-6398-43b5-85d6-65f87361fba",
        "currency": "RUB",
        "amount": 4
    }
    RESPONSE(HttpStatus.NOT_FOUND): Wrong user secret_key

    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency": "VVV",
        "amount": 4
    }
    RESPONSE(HttpStatus.NOT_FOUND): Wrong currency name

- #### Вывод денег. POST запрос. URI **http://localhost:8080/api/balance/withdrawal**

##### Примеры запросов:

    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency": "RUB",
        "amount": 4,
        "wallet": "AsS5A2SASd2as3q5sd2asd53a1s5"
    }
    RESPONSE(HttpStatus.OK):
    {
        "currency": "RUB",
        "amount": 496.0
    }

    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency": "RUB",
        "amount": 4,
        "credit_card": "1234 1243 1234 1243"
    }
    RESPONSE (HttpStatus.OK):
    {
        "currency": "RUB",
        "amount": 492.0
    }

    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency": "RUB",
        "amount": 4444,
        "credit_card": "1234 1243 1234 1243"
    }
    RESPONSE(HttpStatus.BAD_REQUEST): Not enough balance on wallet

- #### Просмотр баланса своего кошелька. GET запрос. URI **http://localhost:8080/api/balance**

##### Примеры запросов:

    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59"
    }
    RESPONSE(HttpStatus.OK):
    [
        {
            "currency": "RUB",
            "amount": 492.0
        },
        {
            "currency": "TON",
            "amount": 0.0
        },
        {
            "currency": "BTC",
            "amount": 0.0
        }
    ]

    REQUEST:
    {
        "secret_key": "89059968-6398-43b5-85d6-65f87361fba",
    }
    RESPONSE(HttpStatus.NOT_FOUND): Wrong user secret_key

    REQUEST(ADMIN secret_key):
    {
        "secret_key": "f24de643-ace3-4224-8534-681d6c329aca"
    }
    RESPONSE(HttpStatus.FORBIDDEN): Access only for USER

---

- #### Просмотр актуальных курсов валют(ADMIN AND USER). GET запрос. URI **http://localhost:8080/api/exchange/rate**

##### Примеры запросов:

    REQUEST:
    {
        "secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
        "currency": "RUB"
    }
    RESPONSE(HttpStatus.OK): 
    [
        {
            "currency": "TON",
            "exchange_rate": 0.0055
        },
        {
            "currency": "BTC",
            "exchange_rate": 5.31E-7
        }
    ]

    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency": "TON"
    }
    RESPONSE(HttpStatus.OK):
    [
        {
            "currency": "RUB",
            "exchange_rate": 180.0
        },
        {
            "currency": "BTC",
            "exchange_rate": 0.0000957
        }
    ]

- #### Обмен валют по установленному курсу. POST запрос. URI **http://localhost:8080/api/balance/exchange**

##### Примеры запросов:

    REQUEST:
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency_from": "RUB",
        "currency_to": "TON",
        "amount": "100"
    }
    RESPONSE(HttpStatus.OK): 
    {
        "currency_from": "RUB",
        "currency_to": "TON",
        "amount_from": 100.0,
        "amount_to": 0.5500
    }

    REQUEST: 
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "currency_from": "RUB",
        "currency_to": "TON",
        "amount": "5555"
    }
    RESPONSE (HttpStatus.BAR_REQUEST): Not enough balance on wallet

## ADMIN API
- #### Изменить курс валют. POST запрос. URI **http://localhost:8080/api/exchange/change-rate**

##### Примеры запросов:

    REQUEST:
    {
        "secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
        "base_currency": "TON",
        "currencies":[
            {
                "currency": "BTC",
                "exchange_rate": "0.000096"
            },
            {
                "currency": "RUB",
                "exchange_rate": "184"
            }
        ]
    }
    RESPONSE (HttpStatus.OK):
    [
        {
            "currency": "BTC",
            "exchange_rate": 0.000096
        },
        {
            "currency": "RUB",
            "exchange_rate": 184.0
        }
    ]

    REQUEST (USER secret_key):
    {
        "secret_key": "a7cac8e5-b69b-4d1b-b2e8-8c2752341d59",
        "base_currency": "TON",
        "currencies":[
            {
                "currency": "BTC",
                "exchange_rate": "0.000096"
            },
            {
                "currency": "RUB",
                "exchange_rate": "184"
            }
        ]
    }
    RESPONSE(HttpStatus.FORBIDDEN): Access only for ADMIN

- #### Посмотреть общую сумму на всех пользовательских счетах для указанной валюты. GET запрос. URI **http://localhost:8080/api/currency/total-amount**

##### Примеры запросов:

    REQUEST:
    {
        "secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
        "currency": "RUB"
    }
    RESPONSE(HttpStatus.OK):
    {
        "currency": "RUB",
        "amount": 392.0
    }

    REQUEST:
    {
        "secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
        "currency": "TON"
    }
    RESPONSE (HttpStatus.OK):
    {
        "currency": "TON",
        "amount": 0.5500
    }

- #### Посмотреть количество операций, которые были проведены за указанный период (например, за последние сутки). GET запрос. URI **http://localhost:8080/api/transaction/count**

##### Примеры запросов:

    REQUEST:
    {
        "secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
        "date_from": "18.02.2022",
        "date_to": "27.02.2023"
    }
    RESPONSE(HttpStatus.OK):
    {
        "transaction_count": 0
    }

    REQUEST:
    {
        "secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
        "date_from": "18.02.2022",
        "date_to": "01.03.2023"
    }
    RESPONSE(HttpStatus.OK):
    {
        "transaction_count": 4
    }