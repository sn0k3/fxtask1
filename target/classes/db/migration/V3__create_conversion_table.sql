CREATE TABLE conversions
(
    id                BIGSERIAL PRIMARY KEY,
    transaction_id    UUID NOT NULL UNIQUE,
    client_id         BIGINT NOT NULL,

    source_amount     NUMERIC(19, 4) NOT NULL,
    source_currency   VARCHAR(3) NOT NULL,

    target_amount     NUMERIC(19, 4) NOT NULL,
    target_currency   VARCHAR(3) NOT NULL,

    rate              NUMERIC(19, 10) NOT NULL,

    idempotency_key   VARCHAR(255) NOT NULL,

    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_conversions_client
        FOREIGN KEY (client_id)
        REFERENCES clients (id),

    CONSTRAINT uk_conversions_client_idempotency
        UNIQUE (client_id, idempotency_key),

    CONSTRAINT chk_conversions_source_amount_positive
        CHECK (source_amount > 0),

    CONSTRAINT chk_conversions_target_amount_positive
        CHECK (target_amount > 0),

    CONSTRAINT chk_conversions_rate_positive
        CHECK (rate > 0),

    CONSTRAINT chk_conversions_source_currency_uppercase
        CHECK (source_currency = UPPER(source_currency)),

    CONSTRAINT chk_conversions_target_currency_uppercase
        CHECK (target_currency = UPPER(target_currency))
);

CREATE INDEX idx_conversions_client_id
    ON conversions (client_id);

CREATE INDEX idx_conversions_created_at
    ON conversions (created_at);

CREATE INDEX idx_conversions_transaction_id
    ON conversions (transaction_id);