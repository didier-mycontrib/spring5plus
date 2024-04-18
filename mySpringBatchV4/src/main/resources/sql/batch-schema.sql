DROP TABLE IF EXISTS functions;
DROP TABLE IF EXISTS person;

CREATE TABLE person  (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    age INTEGER,
    is_active BOOLEAN
);

CREATE TABLE functions  (
    id BIGINT NOT NULL PRIMARY KEY,
    function VARCHAR(50),
    salary DOUBLE
);

ALTER TABLE functions ADD CONSTRAINT functionForValidPerson
FOREIGN KEY(id) REFERENCES person(id);

