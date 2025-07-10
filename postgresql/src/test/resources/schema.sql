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
    field9 DATE
);