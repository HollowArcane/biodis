
INSERT INTO product_category (id, label) VALUES (1, 'Fruits');
INSERT INTO product_category (id, label) VALUES (2, 'Vegetables');
INSERT INTO product_category (id, label) VALUES (3, 'Dairy');

INSERT INTO product_subcategory (id, label, id_product_category) VALUES (1, 'Sub Fruit 1', 1);
INSERT INTO product_subcategory (id, label, id_product_category) VALUES (2, 'Sub Fruit 2', 1);
INSERT INTO product_subcategory (id, label, id_product_category) VALUES (3, 'Sub Vegetables 1', 2);
INSERT INTO product_subcategory (id, label, id_product_category) VALUES (4, 'Sub Vegetables 2', 2);
INSERT INTO product_subcategory (id, label, id_product_category) VALUES (5, 'Sub Dairy', 3);

INSERT INTO product (id, label, id_product_subcategory, threshold_warning) VALUES (1, 'Apple', 1, 100.);
INSERT INTO product (id, label, id_product_subcategory, threshold_warning) VALUES (2, 'Peach', 1, 100.);
INSERT INTO product (id, label, id_product_subcategory, threshold_warning) VALUES (3, 'Orange', 2, 100.);
INSERT INTO product (id, label, id_product_subcategory, threshold_warning) VALUES (4, 'Pumpkin', 4, 100.);
INSERT INTO product (id, label, id_product_subcategory, threshold_warning) VALUES (5, 'Potato', 3, 100.);
INSERT INTO product (id, label, id_product_subcategory, threshold_warning) VALUES (6, 'Milk', 5, 100.);