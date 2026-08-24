# FX Exchange Service - Task submission

Description: A small Spring Boot service for exchanging currencies between client accounts.

The service keeps a separate balance for each client/currency combination,
 gets the exchange rate from Frankfurter, and records completed conversions in PostgreSQL database.

## Running locally

The project uses Java 17 and Maven.

With PostgreSQL running locally, the default connection is:

```text
database: zettafxtask
user: postgres
password: testpassword123
```

Start the application with:

```bash
./mvnw spring-boot:run
```

Flyway creates the tables and inserts the demo data automatically.

The service starts on:

```text
http://localhost:8080
```


## Demo clients

Flyway inserts two clients:

 Client      Currency  Starting balance 
 CLIENT-001  USD            10,000.0000 
 CLIENT-001  EUR             8,000.0000
 CLIENT-002  GBP             5,000.0000


## API endpoints

### Current exchange rate

```http
GET /rates?from=CURRENCY&to=CURRENCY
```

The rate comes from the configured provider.

### Convert currency

```http
POST /conversions
X-Client-Id: CLIENT-001
Idempotency-Key: conversion-001
Content-Type: application/json
```

Request:

```json
{
  "sourceAmount": 100,
  "sourceCurrency": "USD",
  "targetCurrency": "EUR"
}
```

The source balance is debited and the target balance is credited in the same database transaction.

The response contains the transaction ID, amounts, rate, timestamp, and the client's balances after the conversion.

Amounts and exchange rates are represented with `BigDecimal`.

### Client balances

```http
GET /clients/CLIENT-001/balances
```

Example response:

```json
[
  {
    "currency": "EUR",
    "amount": 8000.0000
  },
  {
    "currency": "USD",
    "amount": 10000.0000
  }
]
```

Only currencies that have a balance for the client are returned.

## Idempotency

`POST /conversions` requires an `Idempotency-Key`.

If the same client sends the same key again, the previously created conversion is returned instead of performing another exchange.

The key is also protected by a database unique constraint. This means the guarantee does not depend only on application code.

Example:

```text
X-Client-Id: CLIENT-001
Idempotency-Key: my-request-123
```

Reusing `my-request-123` for the same client does not debit the account a second time.

## Concurrent conversions

Balances are protected with pessimistic database locking.

Before performing a conversion, the service locks all balance rows belonging to the client. Another conversion for the same client waits until the first transaction finishes.

The debit, credit, and conversion record are handled inside one transaction.

## Errors

Some of the relevant responses are:

| HTTP status | Code                 | Meaning                                              |
| ----------- | -------------------- | ---------------------------------------------------- |
| 404         | `CLIENT_NOT_FOUND`   | The supplied client does not exist                   |
| 404         | `BALANCE_NOT_FOUND`  | The client has no balance for the requested currency |
| 422         | `INSUFFICIENT_FUNDS` | The source balance is too small                      |
| 400         | `VALIDATION_ERROR`   | The request body is invalid                          |

## Database

Hibernate is configured with:

```text
ddl-auto=validate
```



The main tables are:

```text
clients
balances
conversions
```

Currency Ammount is stored as PostgreSQL `NUMERIC` and mapped to Java `BigDecimal`.


## Tests
Only two unit tests are implemented.



## TODO:

The taks is not completed fully. There are few things, which left as not implemented:


1) Additional /backup/ service, 
which gathers fx rates, currently using only 1, 
and if for some reason it is down, the app will not work, because
it relies on it for rates.


2) GET /conversions?transactionId=…&date=YYYY-MM-DD&clientId=…&page=…&size=… →
paginated history filtered by transactionId, date, or clientId (at least one filter must be
provided). - This is API endpoint, which left unimplemented.

3) Docker compose file.

4) Integration tests and more unit tests.

5) OpenAPI / Swagger UI auto-generated (e.g. SpringDoc).

