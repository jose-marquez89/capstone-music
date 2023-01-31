CREATE TABLE store (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(30),
	city varchar(40),
	state varchar(2),
	zip varchar(9),
	street varchar(50)
);

CREATE TABLE customer (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(50),
	email varchar(40),
	phone varchar(18)
);

CREATE TABLE product (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(40),	
	price numeric,
	discontinued boolean DEFAULT FALSE
);

CREATE TABLE role (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	type varchar(20)
);

CREATE TABLE service (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(40),
	price numeric,
	discontinued boolean DEFAULT FALSE
);

CREATE TABLE service_application ( 
	service_id integer REFERENCES service (id) ON DELETE SET NULL,
	product_id integer REFERENCES product (id) ON DELETE SET NULL
);

CREATE TABLE employee (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(50),
	start_date timestamp, -- defaults to no time zone
	end_date timestamp, 
	role_id integer REFERENCES role (id)
);

CREATE TABLE manager_detail (
	employee_id integer REFERENCES employee (id),
	user_name varchar(25),
	password varchar(40),
	store_id integer REFERENCES store (id)
);

CREATE TABLE "order" (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP,
	customer_id integer REFERENCES customer (id),
	has_return boolean DEFAULT FALSE,
	processing_employee integer REFERENCES employee (id),
	origin_store_id integer REFERENCES store (id)
);

CREATE TABLE order_line (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	is_service boolean,
	product_id integer REFERENCES product (id) ON DELETE SET NULL,
	service_id integer REFERENCES service (id) ON DELETE SET NULL, 
	order_id integer REFERENCES "order" (id),
	sale_price numeric,
	return boolean DEFAULT FALSE,
	returned_at timestamp
);

CREATE TABLE inventory (
	product_id integer REFERENCES product (id) ON DELETE SET NULL,
	store_id integer REFERENCES store (id),
	on_hand integer DEFAULT 0
);