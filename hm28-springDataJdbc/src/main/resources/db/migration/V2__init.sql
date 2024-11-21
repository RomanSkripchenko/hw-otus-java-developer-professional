CREATE TABLE address (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255)
);

CREATE TABLE phone (
    id SERIAL PRIMARY KEY,
    number VARCHAR(20),
    client_id BIGINT REFERENCES client(id)
);
