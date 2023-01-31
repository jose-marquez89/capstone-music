INSERT INTO role (type)
VALUES 
	('Manager'),
	('Sales Associate');
	

INSERT INTO employee (name, start_date, end_date, role_id)
VALUES
	('Admin Manager', '2020-01-01', NULL, 1),
	('Eric Clapton', '2021-08-23', NULL, 2),
    ('Janice Joplin', '2019-04-19', NULL, 2),
    ('Miles Davis', '2020-02-14', NULL, 1),
    ('Herbie Hancock', '2020-01-23', NULL, 2),
    ('Emily Remler', '2020-05-18', NULL, 2),
    ('John Williams', '2019-12-25', NULL, 1),
    ('Aaron Copland', '2019-11-30', NULL, 2),
    ('Andres Segovia', '2020-03-15', NULL, 2);
	
INSERT INTO customer (name, email, phone)
VALUES 
	('Ted Lasso', 'tlasso@yahoo.com', '(349)-234-0394'),
	('Joe Biden', 'jbiddy@gmail.com', '(916)-422-8066'),
	('Donald Trump', 'dtrump@outlook.com', '(345)-234-5434'),
    ('Condoleezza Rice', 'crice@earthlink.com', '(456)-123-2348'),
    ('Michelle Obama', 'mobama@gmail.com', '(234)-234-3945'),
    ('Laura Bush', 'lbush@outlook.com', '(234)-334-3457');

INSERT INTO store (name, city, state, zip, street)
VALUES 
	('Capstone Music #1', 'Sacramento', 'CA', '95823', '7912 Deer Creek Dr'),
    ('Capstone Music #2', 'Austin', 'TX', '78731', '7015 Village Center Dr'),
    ('Capstone Music #3', 'New York', 'NY', '11221', '236 Malcolm X Blvd');

INSERT INTO product (name, price)
VALUES 
	('Electric Guitar', 499.99),
	('Acoustic Guitar', 599.99),
	('Upright Piano', 1249.99),
	('Grand Piano', 15499.99),
	('Violin', 699.99),
	('Trumpet', 799.99),
    ('Saxophone', 599.99);

INSERT INTO service (name, price)
VALUES 
	('Restring', 15.99),
    ('Cleaning', 9.99),
    ('Tuning', 4.99),
    ('Piano Tuning', 99.99),
    ('Repadding', 49.99);

INSERT INTO service_application (service_id, product_id)
VALUES 
	(1, 1), -- restring
    (1, 2),
    (1, 5),
   	(2, 1), -- cleaning
    (2, 2),
    (2, 3),
    (3, 1), -- tuning
    (3, 2),
    (4, 3), -- piano tuning
	(4, 4),
	(5, 6), -- repadding
	(5, 7);


INSERT INTO inventory (product_id, store_id, on_hand)
VALUES
	-- Sacramento
	(1, 1, 30),
	(2, 1, 30),
	(3, 1, 3),
    (4, 1, 2),
    (5, 1, 10),
    (6, 1, 20),
    (7, 1, 20),
    -- Austin
   	(1, 2, 40),
	(2, 2, 40),
	(3, 2, 2),
    (4, 2, 5),
    (5, 2, 15),
    (6, 2, 24),
    (7, 2, 22),
    -- New York
    (1, 3, 15),
	(2, 3, 25),
	(3, 3, 2),
    (4, 3, 2),
    (5, 3, 10),
    (6, 3, 14),
    (7, 3, 12);

INSERT INTO "order" (customer_id, processing_employee, origin_store_id)
VALUES
	-- Sacramento
	(1, 2, 1),
    (2, 3, 1),
    -- Austin
    (3, 5, 2),
    (4, 6, 2),
    -- New York
    (5, 8, 3),
    (6, 9, 3);

/*
 * 	('Electric Guitar', 499.99),
	('Acoustic Guitar', 599.99),
	('Upright Piano', 1249.99),
	('Grand Piano', 15499.99),
	('Violin', 699.99),
	('Trumpet', 799.99),
    ('Saxophone', 599.99);

	('Restring', 15.99),
    ('Cleaning', 9.99),
    ('Tuning', 4.99),
    ('Piano Tuning', 99.99),
    ('Repadding', 49.99);
 */
INSERT INTO order_line (is_service, product_id, service_id, order_id, sale_price, return)
VALUES
	-- Sacramento, order 1
	(FALSE, 1, NULL, 1, 499.99, FALSE),
	(TRUE, NULL, 1, 1, 15.99, FALSE),
	-- So2
	(FALSE, 2, NULL, 2, 599.99, FALSE),
    (FALSE, 3, NULL, 2, 1249.99, FALSE),
    -- Austin order 3
    (FALSE, 1, NULL, 3, 499.99, FALSE),
	(TRUE, NULL, 1, 3, 15.99, FALSE),
    -- Ao4
    (FALSE, 4, NULL, 4, 15499.99, FALSE),
    -- New York o5
    (FALSE, 2, NULL, 5, 599.99, FALSE),
    (TRUE, NULL, 1, 5, 15.99, FALSE),
    -- o6
    (FALSE, 6, NULL, 6, 799.99, FALSE),
    (FALSE, 6, NULL, 6, 799.99, FALSE),
    (FALSE, 6, NULL, 6, 799.99, FALSE),
   	(FALSE, 6, NULL, 6, 799.99, FALSE),
    (FALSE, 7, NULL, 6, 599.99, FALSE);
