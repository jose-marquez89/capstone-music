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
	name varchar(),
	price numeric
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
	id integer PRIMARY KEY  GENERATED ALWAYS AS IDENTITY,
	product_id integer,
	name varchar(40)	
)








