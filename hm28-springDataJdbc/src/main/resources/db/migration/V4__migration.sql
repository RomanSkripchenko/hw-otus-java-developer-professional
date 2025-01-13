-- Создание схемы, если она не существует
CREATE SCHEMA IF NOT EXISTS my_schema;

-- Создание таблицы client, если она не существует
CREATE TABLE IF NOT EXISTS my_schema.client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address_id BIGINT
);

-- Создание таблицы address, если она не существует
CREATE TABLE IF NOT EXISTS my_schema.address (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT fk_client_address FOREIGN KEY (client_id) REFERENCES my_schema.client (id)
);

-- Создание таблицы phone, если она не существует
CREATE TABLE IF NOT EXISTS my_schema.phone (
    id SERIAL PRIMARY KEY,
    number VARCHAR(20) NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT fk_client_phone FOREIGN KEY (client_id) REFERENCES my_schema.client (id)
);

-- Обновляем связь client -> address, проверяя существование ограничения
DO $$ BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_address_client') THEN
    ALTER TABLE my_schema.client
    ADD CONSTRAINT fk_address_client FOREIGN KEY (address_id) REFERENCES my_schema.address (id) ON DELETE SET NULL;
  END IF;
END $$;
