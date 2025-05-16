-- Создание таблицы "artist" (художники)
CREATE TABLE artist (
    artist_id SERIAL PRIMARY KEY,
    artist_name VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    birth_year INT CHECK (birth_year BETWEEN 1000 AND EXTRACT(YEAR FROM NOW())),
    death_year INT CHECK (death_year > birth_year OR death_year IS NULL)
);

-- Создание таблицы "style" (стили)
CREATE TABLE style (
    style_id SERIAL PRIMARY KEY,
    style_name VARCHAR(50) NOT NULL UNIQUE
);

-- Создание таблицы "artwork" (произведения искусства)
CREATE TABLE artwork (
    artwork_id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    year INT NOT NULL CHECK (year BETWEEN 1000 AND EXTRACT(YEAR FROM NOW())),
    image_path VARCHAR(255) NOT NULL,
    artist_id INT NOT NULL REFERENCES artist(artist_id) ON DELETE CASCADE,
    style_id INT NOT NULL REFERENCES style(style_id) ON DELETE CASCADE
);

-- Создание таблицы "exhibition" (выставки)
CREATE TABLE exhibition (
    exhibition_id SERIAL PRIMARY KEY,
    exhibition_name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Создание таблицы "exhibition_artwork" (связь многие-ко-многим)
CREATE TABLE exhibition_artwork (
    exhibition_id INT NOT NULL REFERENCES exhibition(exhibition_id) ON DELETE CASCADE,
    artwork_id INT NOT NULL REFERENCES artwork(artwork_id) ON DELETE CASCADE,
    PRIMARY KEY (exhibition_id, artwork_id)
);

-- Создание таблицы "user" (пользователи)
CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Создание таблицы "favorite" (избранное)
CREATE TABLE favorite (
    user_id INT NOT NULL REFERENCES user_data(user_id) ON DELETE CASCADE,
    artwork_id INT NOT NULL REFERENCES artwork(artwork_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, artwork_id)
);

-- Создание таблицы "admin" (администраторы)
CREATE TABLE "admin" (
    adminID SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


-- Добавляем новый столбец description
ALTER TABLE artwork ADD COLUMN description TEXT;

-- Добавление ограничений на пароль
ALTER TABLE "user"
ADD CONSTRAINT password_min_length 
CHECK (LENGTH(password) >= 4);

ALTER TABLE "admin"
ADD CONSTRAINT password_min_length 
CHECK (LENGTH(password) >= 4);

