\c postgres;
DROP DATABASE biodis;

CREATE DATABASE biodis;
\c biodis;

-- PRODUCT:
    DROP TABLE IF EXISTS product_category CASCADE;
    CREATE TABLE product_category(
        id SERIAL PRIMARY KEY,
        label VARCHAR(255) NOT NULL UNIQUE,
        CHECK(TRIM(label) <> '')
    );

    DROP TABLE IF EXISTS product_subcategory CASCADE;
    CREATE TABLE product_subcategory(
        id SERIAL PRIMARY KEY,
        label VARCHAR(255) NOT NULL UNIQUE,
        id_product_category INT REFERENCES product_category(id) ON DELETE CASCADE,
        CHECK(TRIM(label) <> '')
    );

    DROP TABLE IF EXISTS product CASCADE;
    CREATE TABLE product(
        id SERIAL PRIMARY KEY,
        label VARCHAR(255) NOT NULL UNIQUE,
        id_product_subcategory INT REFERENCES product_subcategory(id) ON DELETE CASCADE,
        threshold_warning DECIMAL(18, 5) NOT NULL,
        CHECK(TRIM(label) <> ''),
        CHECK(threshold_warning >= 0)
    );

-- STAFF
    DROP TABLE IF EXISTS staff CASCADE;
    CREATE TABLE staff( 
        id SERIAL PRIMARY KEY,
        first_name VARCHAR(255),
        last_name VARCHAR(255) NOT NULL,
        phone_number VARCHAR(15),
        email VARCHAR(255),
        UNIQUE(first_name, last_name),
        CHECK(TRIM(first_name) <> ''),
        CHECK(phone_number IS NOT NULL OR email IS NOT NULL),
        CHECK(phone_number IS NULL OR phone_number ~ '^[+-]?[0-9]{10}$') ,
        CHECK(email IS NULL OR email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$')
    );

-- STOCK
    DROP TABLE IF EXISTS mvt_product_stock CASCADE;
    CREATE TABLE mvt_product_stock(
        id SERIAL PRIMARY KEY,
        id_product INT REFERENCES product(id) ON DELETE CASCADE,
        quantity_in DECIMAL(18, 5) NOT NULL,
        quantity_out DECIMAL(18, 5) NOT NULL,
        date DATE DEFAULT NOW(),
        CHECK(quantity_in >= 0),
        CHECK(quantity_out >= 0)
    );

    DROP TABLE IF EXISTS stock_withdrawal CASCADE;
    CREATE TABLE stock_withdrawal(
        id SERIAL PRIMARY KEY,
        date DATE NOT NULL DEFAULT NOW(),
        reason VARCHAR(255) NOT NULL,
        id_staff INT NOT NULL REFERENCES staff(id) ON DELETE CASCADE
    );

    DROP TABLE IF EXISTS stock_withdrawal_detail CASCADE;
    CREATE TABLE stock_withdrawal_detail(
        id_product INT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
        id_stock_withdrawal INT NOT NULL REFERENCES stock_withdrawal(id) ON DELETE CASCADE,
        quantity NUMERIC(18, 5),
        price_unit NUMERIC(18, 5),
        CHECK(quantity >= 0),
        CHECK(price_unit >= 0)
    );

-- SALE
    DROP TABLE IF EXISTS customer CASCADE;
    CREATE TABLE customer( 
        id SERIAL PRIMARY KEY,
        first_name VARCHAR(255),
        last_name VARCHAR(255) NOT NULL,
        phone_number VARCHAR(15),
        email VARCHAR(255),
        UNIQUE(first_name, last_name),
        CHECK(TRIM(first_name) <> ''),
        CHECK(phone_number IS NOT NULL OR email IS NOT NULL),
        CHECK(phone_number IS NULL OR phone_number ~ '^[+-]?[0-9]{10}$') ,
        CHECK(email IS NULL OR email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$')
    );

    DROP TABLE IF EXISTS invoice CASCADE;
    CREATE TABLE invoice(
        id SERIAL PRIMARY KEY,
        date DATE NOT NULL DEFAULT NOW(),
        id_customer INT NOT NULL REFERENCES customer(id) ON DELETE CASCADE,
        id_stock_withdrawal INT NOT NULL REFERENCES stock_withdrawal(id) ON DELETE CASCADE
    );