CREATE DATABASE IF NOT EXISTS mydb DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE mydb;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO user (name, email, age) VALUES ('Alice', 'alice@example.com', 25);
INSERT INTO user (name, email, age) VALUES ('Bob', 'bobb@example.com', 30);