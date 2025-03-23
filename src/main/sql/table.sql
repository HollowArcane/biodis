CREATE DATABASE biodis;
\c biodis;

DROP TABLE IF EXISTS product_category CASCADE;
CREATE TABLE product_category(
    id SERIAL PRIMARY KEY,
    label VARCHAR(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS product_subcategory CASCADE;
CREATE TABLE product_subcategory(
    id SERIAL PRIMARY KEY,
    label VARCHAR(255) NOT NULL UNIQUE,
    id_product_category INT REFERENCES product_category(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS product CASCADE;
CREATE TABLE product(
    id SERIAL PRIMARY KEY,
    label VARCHAR(255) NOT NULL UNIQUE,
    id_product_subcategory INT REFERENCES product_subcategory(id) ON DELETE CASCADE,
    threshold_warning DECIMAL(18, 5) NOT NULL
);


DROP TABLE IF EXISTS mvt_product_stock CASCADE; -- positive for entry, negative for withdraw
CREATE TABLE mvt_product_stock(
    id SERIAL PRIMARY KEY,
    id_product INT REFERENCES product(id) ON DELETE CASCADE,
    quantity_in DECIMAL(18, 5) NOT NULL,
    quantity_out DECIMAL(18, 5) NOT NULL,
    date DATE DEFAULT NOW()
);