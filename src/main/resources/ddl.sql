-- DATABASE fernanda_imansyah

-- public.items definition

-- Drop table

-- DROP TABLE items;

CREATE TABLE items (
	id bigserial NOT NULL,
	created_by varchar(255) NULL,
	created_date timestamp NULL,
	updated_by varchar(255) NULL,
	updated_date timestamp NULL,
	"cost" int4 NULL,
	description varchar(500) NULL,
	"name" varchar(500) NULL,
	price int4 NULL,
	CONSTRAINT items_pkey PRIMARY KEY (id)
);


-- public.po_header definition

-- Drop table

-- DROP TABLE po_header;

CREATE TABLE po_header (
	id bigserial NOT NULL,
	created_by varchar(255) NULL,
	created_date timestamp NULL,
	updated_by varchar(255) NULL,
	updated_date timestamp NULL,
	date_time timestamp NULL,
	description varchar(500) NULL,
	total_cost int4 NULL,
	total_price int4 NULL,
	CONSTRAINT po_header_pkey PRIMARY KEY (id)
);


-- public.users definition

-- Drop table

-- DROP TABLE users;

CREATE TABLE users (
	id bigserial NOT NULL,
	created_by varchar(255) NULL,
	created_date timestamp NULL,
	updated_by varchar(255) NULL,
	updated_date timestamp NULL,
	email varchar(255) NULL,
	first_name varchar(500) NOT NULL,
	last_name varchar(500) NULL,
	phone varchar(255) NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.po_detail definition

-- Drop table

-- DROP TABLE po_detail;

CREATE TABLE po_detail (
	id bigserial NOT NULL,
	"cost" int4 NULL,
	item_id int8 NULL,
	po_header_id int8 NULL,
	price int4 NULL,
	quantity int4 NULL,
	CONSTRAINT po_detail_pkey PRIMARY KEY (id),
	CONSTRAINT fk7t2orrs6fqc20le5fjrtgwmhr FOREIGN KEY (po_header_id) REFERENCES po_header(id)
);