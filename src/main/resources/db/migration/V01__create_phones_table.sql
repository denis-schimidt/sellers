CREATE TABLE IF NOT EXISTS phones (
    seller_id BIGINT NOT NULL,
    area_code SMALLINT NOT NULL,
    number INTEGER NOT NULL,
    type VARCHAR(15) NOT NULL CHECK (type IN ('HOME', 'CELL_PHONE', 'WORK')),
    PRIMARY KEY (area_code, number)
);
