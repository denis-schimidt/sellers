CREATE TABLE IF NOT EXISTS sellers (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('IN_ANALYSIS', 'BLOCKED', 'ACTIVE', 'INACTIVE')),
    email VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    PRIMARY KEY (id)
);
