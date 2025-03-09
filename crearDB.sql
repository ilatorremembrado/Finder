-- Base de datos y tablas para Finder
CREATE DATABASE IF NOT EXISTS finder_db;
USE finder_db;

-- Tabla de usuarios (se cambia profile_image a TEXT para almacenar base64)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_image LONGTEXT
);

-- Tabla de likes
CREATE TABLE IF NOT EXISTS likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_from_id BIGINT,
    user_to_id BIGINT,
    liked BOOLEAN,
    FOREIGN KEY (user_from_id) REFERENCES users(id),
    FOREIGN KEY (user_to_id) REFERENCES users(id)
);

-- Tabla de matches
CREATE TABLE IF NOT EXISTS matches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user1_id BIGINT,
    user2_id BIGINT,
    match_date DATETIME,
    FOREIGN KEY (user1_id) REFERENCES users(id),
    FOREIGN KEY (user2_id) REFERENCES users(id)
);

-- Tabla de mensajes
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT,
    receiver_id BIGINT,
    content TEXT,
    timestamp DATETIME,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);