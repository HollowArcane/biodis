-- PRODUCT
    DROP VIEW IF EXISTS v_stock_product CASCADE;
    CREATE OR REPLACE VIEW v_stock_product AS
        SELECT
            p.id,
            p.label,
            p.id_product_subcategory,
            p.threshold_warning,
            COALESCE(SUM(mps.quantity_in), 0) AS quantity_in,
            COALESCE(SUM(mps.quantity_out), 0) AS quantity_out
        FROM
            product AS p
        LEFT JOIN
            mvt_product_stock AS mps ON p.id = mps.id_product
        GROUP BY
            p.id,
            p.label,
            p.id_product_subcategory,
            p.threshold_warning
    ;

    DROP VIEW IF EXISTS v_label_product_subcategory CASCADE;
    CREATE OR REPLACE VIEW v_label_product_subcategory AS
        SELECT
            ps.id,
            ps.label AS product_subcategory,
            ps.id_product_category,
            pc.label AS product_category
        FROM
            product_subcategory AS ps
        JOIN
            product_category AS pc ON ps.id_product_category = pc.id
    ;

    DROP VIEW IF EXISTS v_label_product CASCADE;
    CREATE OR REPLACE VIEW v_label_product AS
        SELECT
            p.id,
            p.label AS product,
            p.id_product_subcategory,
            ps.label AS product_subcategory,
            ps.id_product_category,
            pc.label AS product_category,
            p.threshold_warning,
            vsp.quantity_in,
            vsp.quantity_out
        FROM
            product AS p
        JOIN
            product_subcategory AS ps ON p.id_product_subcategory = ps.id
        JOIN
            product_category AS pc ON ps.id_product_category = pc.id
        JOIN
            v_stock_product vsp ON p.id = vsp.id
    ;

-- STOCK
    DROP VIEW IF EXISTS v_label_mvt_product_stock CASCADE;
    CREATE OR REPLACE VIEW v_label_mvt_product_stock AS
        SELECT
            mps.id,
            mps.id_product,
            p.label AS product,
            p.id_product_subcategory,
            ps.label AS product_subcategory,
            ps.id_product_category,
            pc.label AS product_category,
            p.threshold_warning,
            mps.quantity_in,
            mps.quantity_out,
            mps.date
        FROM
            mvt_product_stock AS mps
        JOIN
            product AS p ON mps.id_product = p.id
        JOIN
            product_subcategory AS ps ON p.id_product_subcategory = ps.id
        JOIN
            product_category AS pc ON ps.id_product_category = pc.id
    ;