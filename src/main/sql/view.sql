CREATE OR REPLACE VIEW v_label_mvt_product_stock AS
    SELECT
        mps.id,
        mps.id_product,
        p.label AS product,
        p.id_product_subcategory,
        ps.label AS product_subcategory,
        ps.id_product_category,
        pc.label AS product_category,
        mps.quantity,
        mps.date
    FROM
        mvt_product_stock mps
    JOIN
        product p ON mps.id_product = p.id
    JOIN
        product_subcategory ps ON p.id_product_subcategory = ps.id
    JOIN
        product_category pc ON ps.id_product_category = pc.id
;

CREATE OR REPLACE VIEW v_mvt_product_stock_daily_balance AS
    SELECT
        mps.id_product,
        p.label AS product,
        p.id_product_subcategory,
        ps.label AS product_subcategory,
        ps.id_product_category,
        pc.label AS product_category,
        SUM(CASE WHEN mps.quantity < 0 THEN -mps.quantity ELSE 0 END) AS quantity_withdraw,
        SUM(CASE WHEN mps.quantity > 0 THEN mps.quantity ELSE 0 END) AS quantity_entry,
        (
            SELECT SUM(_mps.quantity) 
            FROM mvt_product_stock _mps 
            WHERE _mps.id_product = mps.id_product AND _mps.date <= mps.date
        ) AS quantity_balance,
        mps.date
    FROM
        mvt_product_stock mps
    JOIN
        product p ON mps.id_product = p.id
    JOIN
        product_subcategory ps ON p.id_product_subcategory = ps.id
    JOIN
        product_category pc ON ps.id_product_category = pc.id
    GROUP BY
        mps.date, 
        mps.id_product,
        p.label,
        p.id_product_subcategory,
        ps.label,
        ps.id_product_category,
        pc.label
;
