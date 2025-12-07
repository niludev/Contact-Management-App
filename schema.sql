CREATE TABLE IF NOT EXISTS contacts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS contact_phone_numbers (
    id BIGSERIAL PRIMARY KEY,
    contact_id BIGINT NOT NULL,
    phone_number VARCHAR(50) NOT NULL,

    CONSTRAINT fk_contact FOREIGN KEY (contact_id) REFERENCES contacts(id) ON DELETE CASCADE
);
