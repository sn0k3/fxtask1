CREATE TABLE balances
(
    id              BIGSERIAL PRIMARY KEY,
    client_id       BIGINT NOT NULL,
    currency        VARCHAR(3) NOT NULL,
    amount          NUMERIC(19, 4) NOT NULL DEFAULT 0,

    CONSTRAINT fk_balances_client
        FOREIGN KEY (client_id)
        REFERENCES clients (id)
        ON DELETE CASCADE,

    CONSTRAINT uk_balances_client_currency
        UNIQUE (client_id, currency),

    CONSTRAINT chk_balances_amount_non_negative
        CHECK (amount >= 0),

    CONSTRAINT chk_balances_currency_uppercase
        CHECK (currency = UPPER(currency))
);

CREATE INDEX idx_balances_client_id
    ON balances (client_id);