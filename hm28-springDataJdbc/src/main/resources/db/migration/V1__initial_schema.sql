-- Создаем схему, если она еще не существует
CREATE SCHEMA IF NOT EXISTS my_schema;

-- Создаем таблицу client
CREATE TABLE my_schema.client (
    id SERIAL PRIMARY KEY,         -- Уникальный идентификатор клиента
    name VARCHAR(255) NOT NULL,    -- Имя клиента
    address_id BIGINT              -- Внешний ключ на таблицу address
);

-- Создаем таблицу address
CREATE TABLE my_schema.address (
    id SERIAL PRIMARY KEY,         -- Уникальный идентификатор адреса
    street VARCHAR(255) NOT NULL,  -- Улица
    client_id BIGINT NOT NULL,     -- Внешний ключ на таблицу client
    CONSTRAINT fk_client_address FOREIGN KEY (client_id) REFERENCES my_schema.client (id)
);

-- Создаем таблицу phone
CREATE TABLE my_schema.phone (
    id SERIAL PRIMARY KEY,         -- Уникальный идентификатор телефона
    number VARCHAR(20) NOT NULL,   -- Номер телефона
    client_id BIGINT NOT NULL,     -- Внешний ключ на таблицу client
    CONSTRAINT fk_client_phone FOREIGN KEY (client_id) REFERENCES my_schema.client (id)
);

-- Обновляем связь client -> address
ALTER TABLE my_schema.client
    ADD CONSTRAINT fk_address_client FOREIGN KEY (address_id) REFERENCES my_schema.address (id);
