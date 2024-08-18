-- Active: 1722845528422@@127.0.0.1@3306@api_database
CREATE DATABASE api_database;

USE api_database;

CREATE TABLE role (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

INSERT INTO role (role_name) VALUES ('ADMIN'), ('USER');
SELECT * FROM role;

CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email_is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    phone_is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    profile_pic_url VARCHAR(255),
    response_token VARCHAR(255),
    otp VARCHAR(100)
    
);


SELECT * FROM user;

CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE
);


CREATE TABLE verification_token (
    verification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
    
);

SELECT * FROM verification_token;

