CREATE TABLE store (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(30),
	city varchar(40),
	state varchar(2),
	zip varchar(9),
	street varchar(50)
)

CREATE TABLE customer (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(50),
	email varchar(40),
	phone varchar(18)
)

CREATE TABLE product (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(40)	
	price numeric
)

CREATE TABLE role (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	type varchar(20)
)

CREATE TABLE service (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	product_id integer REFERENCES product (id),
	name varchar(40)	
)

CREATE TABLE employee (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(50),
	start_date timestamp, -- defaults to no time zone
	end_date timestamp, 
	role_id integer REFERENCES role (id)
)

CREATE TABLE manager_credential (
	employee_id integer REFERENCES employee (id),
	user_name varchar(25),
	password varchar(40)
)

CREATE TABLE order (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP(),
	customer_id integer REFERNECES customer (id),
	has_return boolean,
	processing_employee integer REFERENCES employee (id),
	origin_store_id integer REFERENCES store (id)
)

CREATE TABLE order_line (
	id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	is_service boolean,
	product_id integer REFERENCES product (id),
	service_id integer REFERENCES service (id),
	order_id integer REFERENCES order (id),
	return boolean,
	returned_at timestamp
)

CREATE TABLE inventory (
	product_id integer REFERENCES product (id),
	store_id integer REFERENCES store (id),
	on_hand integer DEFAULT 0
)











