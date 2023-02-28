# Crypto-exchange
Тестовый проект релэкс. Реализован RESTfull API service крипто-биржа.

## Использованные технологии:
- ### Java 17
- ### Maven 3.8.7
- ### Spring-boot 3.0.2
- ### PostgreSQL 42.5.4
- ### flyway 9.7.0
- ### lombok 1.18.26

## Реализованные требования
- #### По умолчанию реализован полный API для USER и ADMIN (примеры запросов ниже)
### Дополтнительные требования:
- #### подключить базу данных Sqlite или PostgreSQL для хранения данных о балансе пользовательских кошельков и истории операций;
- #### сервис по запросу может возвращать данные в json ИЛИ xml.
- #### Формат может быть изменен добавлением header путем добавления accept:application/json или accept:application/xml к HTTP-запросу.
### Есть изменения в теле ответов и запросов, по сравнению с примерами ТЗ (изменено по согласованию).

## Запуск
Для запуска приложения необходимо иметь предустановленный PostreSQL
на компьтере. Порядок:
- Создание базы данных postgreSQL
- Изменения параметров в application.properties, а именно ввод
названия созданной бд, имя пользователя и его пароль.

## Есть данные по умолчанию:
- ### ADMIN с secret_key f24de643-ace3-4224-8534-681d6c329aca
- ### 3 базовые валюты: RUB, TON, BTC с курсами относительно друг друга.

## USER API:
- ### Регистрация пользователя. POST запрос. URI **http://localhost:8080/api/user**

Примеры запросов:

REQUEST:
{
  "username": "ff",
  "email": "sgga@mail.ru"
}

RESPONSE (HttpStatus.OK):
{
    "secret_key": "212064b9-4e59-4db3-a7cd-34ac97baa046"
}

---
REQUEST:
{
"username": "fff2",
"email": "sgga@mail.ru"
}

RESPONSE (HttpStatus.BAD_REQUEST):
Email already present

---
REQUEST:
{
"username": "fff",
"email": "sggad@mail.ru"
}

RESPONSE (HttpStatus.BAD_REQUEST): Username already present

---
REQUEST: 
{
"username": "fff"
}

RESPONSE (HttpStatus.BAD_REQUEST): Enter your email

---

REQUEST:
{
    "email": "sgghad@mail.ru"
}

RESPONSE (HttpStatus.BAD_REQUEST): Enter your username

- ### Пополнение баланса. POST запрос. URI **http://localhost:8080/api/balance/replenishment**

Примеры запросов:

REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361efba",
"currency": "RUB",
"amount": "20"
}

RESPONSE (HttpStatus.OK):
{
"amount": 20.0,
"currency": "RUB"
}

---
REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361efba",
"currency": "RUB",
}

RESPONSE (HttpStatus.BAD_REQUEST): Amount should not be empty

---
REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361fba",
"currency": "RUB",
"amount": 4
}

RESPONSE (HttpStatus.NOT_FOUND): Wrong user secret_key

---

REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361fba",
"currency": "VVV",
"amount": 4
}

RESPONSE (HttpStatus.NOT_FOUND): Wrong currency name

- ### Вывод денег. POST запрос. URI **http://localhost:8080/api/balance/withdrawal**

Примеры запросов:

REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361efba",
"currency": "RUB",
"amount": 4,
"credit_card": "0044 0004 0004 0004"
}

RESPONSE (HttpStatus.OK):
{
"amount": 16.0,
"currency": "RUB"
}

---
REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361efba",
"currency": "RUB",
"amount": 4,
"wallet": "AsS5A2SASd2as3q5sd2asd53a1s5"
}

RESPONSE (HttpStatus.OK):
{
"amount": 12.0,
"currency": "RUB"
}

---

REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361efba",
"currency": "RUB",
"amount": 300,
"wallet": "AsS5A2SASd2as3q5sd2asd53a1s5"
}

RESPONSE (HttpStatus.BAD_REQUEST): Not enough balance on wallet

- ### Просмотр баланса своего кошелька. GET запрос. URI **http://localhost:8080/api/balance**

Примеры запросов:

REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361efba"
}

RESPONSE(HttpStatus.OK):
[
{
"amount": 12.0,
"currency": "RUB"
},
{
"amount": 0.0,
"currency": "TON"
},
{
"amount": 0.0,
"currency": "BTC"
}
]

---
REQUEST:
{
"secret_key": "89059968-6398-43b5-85d6-65f87361fba",
}

RESPONSE (HttpStatus.NOT_FOUND): Wrong user secret_key

---
REQUEST (ADMIN secret_key):
{
"secret_key": "f24de643-ace3-4224-8534-681d6c329aca"
}

RESPONSE (HttpStatus.FORBIDDEN): Access only for USER

---

- ### Просмотр актуальных курсов валют(ADMIN AND USER). GET запрос. URI **http://localhost:8080/api/currency/exchange-rate**

Примеры запросов:

REQUEST:
{
"secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
"currency": "RUB"
}

RESPONSE (HttpStatus.OK): 
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

---

REQUEST:
{
"secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
"currency": "TON"
}

RESPONSE (HttpStatus.OK):
[
{
"currency": "RUB",
"exchange_rate": 180.0
},
{
"currency": "BTC",
"exchange_rate": 9.57E-5
}
]

- ### Обмен валют по установленному курсу. POST запрос. URI **http://localhost:8080/api/balance/exchange**

Примеры запросов:

REQUEST: {
"secret_key": "89059968-6398-43b5-85d6-65f87361efba",
"currency_from": "RUB",
"currency_to": "TON",
"amount": "2000"
}

RESPONSE (HttpStatus.BAD_REQUEST): Not enough balance on wallet

---
REQUEST: {
"secret_key": "89059968-6398-43b5-85d6-65f87361efba",
"currency_from": "RUB",
"currency_to": "TON",
"amount": "4"
}

RESPONSE (HttpStatus.OK):
{
"amount_from": 4.0,
"amount_to": 0.022,
"currency_from": "RUB",
"currency_to": "TON"
}

---

## ADMIN API
- ### Изменить курс валют. POST запрос. URI **http://localhost:8080/api/currency/change-exchange-rate**

Примеры запросов:

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
"exchange_rate": 9.6E-5
},
{
"currency": "RUB",
"exchange_rate": 184.0
}
]

---
REQUEST (USER secret_key):
{
"secret_key": "89059968-6398-43b5-85d6-65f87361efba",
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

RESPONSE (HttpStatus.FORBIDDEN): Access only for ADMIN

- ### Посмотреть общую сумму на всех пользовательских счетах для указанной валюты. GET запрос. URI **http://localhost:8080/api/currency/total-currency-amount**

Примеры запросов:

REQUEST:
{
"secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
"currency": "RUB"
}

RESPONSE(HttpStatus.OK):
{
"amount": 4.0,
"currency": "RUB"
}

---
REQUEST:
{
"secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
"currency": "TON"
}

RESPONSE (HttpStatus.OK):
{
"amount": 0.044,
"currency": "TON"
}

- ### Посмотреть количество операций, которые были проведены за указанный период (например, за последние сутки). GET запрос. URI **http://localhost:8080/api/transaction/count**

Примеры запросов:

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

---
REQUEST:
{
"secret_key": "f24de643-ace3-4224-8534-681d6c329aca",
"date_from": "18.02.2022",
"date_to": "28.02.2023"
}

RESPONSE(HttpStatus.OK):
{
"transaction_count": 16
}