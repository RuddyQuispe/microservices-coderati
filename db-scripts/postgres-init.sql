CREATE DATABASE "orderdb"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Conéctate a la base de datos recién creada
\connect orderdb;