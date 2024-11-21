ALTER TABLE address ADD COLUMN client_id BIGINT REFERENCES client(id);
