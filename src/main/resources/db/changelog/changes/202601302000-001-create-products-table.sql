-- liquibase formatted sql
-- changeset products-api:202601302000-001

CREATE TABLE public.tb_product (
	id uuid NOT NULL,
	name text NOT NULL,
	price decimal(10,2) NOT NULL,
	description text,
	created_at timestamp,
	updated_at timestamp,
	CONSTRAINT chk_price_positive CHECK (price >= 0),
	CONSTRAINT tb_product_pk PRIMARY KEY (id)
);

ALTER TABLE public.tb_product OWNER TO postgres;

CREATE INDEX tb_product_created_at_idx ON public.tb_product
USING btree
(
	created_at
);