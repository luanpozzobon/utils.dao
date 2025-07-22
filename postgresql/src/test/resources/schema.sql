DROP SCHEMA public CASCADE;

CREATE SCHEMA public;

CREATE TABLE public.test (
    id uuid PRIMARY KEY,
    field1 VARCHAR(255),
    field2 INTEGER,
    field3 BIGINT,
    field4 FLOAT,
    field5 DOUBLE PRECISION,
    field6 BOOLEAN,
    field7 NUMERIC,
    field8 SMALLINT,
    field9 DATE,
    entity_id VARCHAR(255)
);

CREATE TABLE public.test2 (
    id VARCHAR(255) PRIMARY KEY,
    field1 VARCHAR(255)
);

ALTER TABLE public.test ADD CONSTRAINT fk_test_entity FOREIGN KEY (entity_id) REFERENCES public.test2 (id);