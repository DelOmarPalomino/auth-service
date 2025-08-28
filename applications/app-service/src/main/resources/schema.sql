CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30),
    address VARCHAR(255),
    birth_date DATE,
    base_salary NUMERIC(12, 2) NOT NULL CHECK (
        base_salary >= 0
        AND base_salary <= 1500000
    )
);