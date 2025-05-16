-- Добавление художников
INSERT INTO artist (artist_name, country, birth_year, death_year) VALUES
('Клод Моне', 'Франция', 1840, 1926),
('Леонардо да Винчи', 'Италия', 1452, 1519),
('Фрида Кало', 'Мексика', 1907, 1954),
('Винсент Ван Гог', 'Нидерланды', 1853, 1890),
('Пабло Пикассо', 'Испания', 1881, 1973),
('Анри Матисс', 'Франция', 1869, 1954),
('Анри Море', 'Франция', 1846, 1921),
('Пьер Огюст Ренуар', 'Франция', 1841, 1919),
('Сандро Боттичелли', 'Италия', 1445, 1510),
('Микеланджело', 'Италия', 1475, 1564),
('Ив Кляйн', 'Франция', 1928, 1962);


-- Добавление стилей
INSERT INTO style (style_name) VALUES
('Импрессионизм'),
('Ренессанс'),
('Современное искусство'),
('Постимпрессионизм'), 
('Кубизм'),
('Фовизм'),           
('Сюрреализм');


-- Добавление произведений
INSERT INTO artwork (title, year, image_path, artist_id, style_id) VALUES
-- Импрессионизм
('Впечатление. Восход солнца', 1872, '/images/impression_sunrise.jpg', 1, 1),
('Дама в саду Сент-Адресс', 1867, '/images/lady.jpg', 1, 1),
('Пор-Манеш', 1896, '/images/por_manesh.jpg', 7, 1),
('Завтрак гребцов', 1880, '/images/Luncheon_of_the_Boating.jpg', 8, 1), 

-- Ренессанс
('Мона Лиза', 1503, '/images/mona_liza.jpg', 2, 2),
('Рождение Венеры', 1482, '/images/venera.jpg', 9, 2),  -- Боттичелли
('Дама с горностаем', 1452, '/images/lady_with_gorn.jpg', 2, 2),
('Сотворение Адама', 1508, '/images/Creation_Of_Adam.jpg', 10, 2), -- Микеланджело

-- Постимпрессионизм
('Звездная ночь', 1889, '/images/starry_night.jpg', 4, 4),  -- Ван Гог

-- Фовизм
('Танец', 1910, '/images/dance.jpg', 6, 6),  -- Матисс

-- Сюрреализм
('Две Фриды', 1939, '/images/two.jpg', 3, 7),  -- Фрида Кало

-- Кубизм
('Антропометрия голубого периода', 1960, '/images/ив_кляйн.jpg', 11, 5);  -- Ив Кляйн
       

-- Добавление выставок
INSERT INTO exhibition (exhibition_name, description) VALUES
('Импрессионизм', 'В попытке поймать ускользающее мгновение жизни'),
('Ренессанс', 'Шедевры эпохи Возрождения'),
('Современное искусство', 'Работы в ногу со временем');

-- Связь выставок и произведений
INSERT INTO exhibition_artwork (exhibition_id, artwork_id)
VALUES
-- Импрессионизм
(
    (SELECT e.exhibition_id  FROM exhibition e WHERE e.exhibition_name = 'Импрессионизм'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Впечатление. Восход солнца')
),
(
    (SELECT e.exhibition_id  FROM exhibition e WHERE e.exhibition_name = 'Импрессионизм'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Дама в саду Сент-Адресс')
),
(
    (SELECT e.exhibition_id  FROM exhibition e WHERE e.exhibition_name = 'Импрессионизм'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Пор-Манеш')
),
(
    (SELECT e.exhibition_id  FROM exhibition e WHERE e.exhibition_name = 'Импрессионизм'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Завтрак гребцов')
),
(
    (SELECT e.exhibition_id  FROM exhibition e WHERE e.exhibition_name = 'Импрессионизм'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Звездная ночь')
),
(
    (SELECT e.exhibition_id  FROM exhibition e WHERE e.exhibition_name = 'Импрессионизм'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Танец')
),

-- Ренессанс
(
    (SELECT e.exhibition_id FROM exhibition e WHERE e.exhibition_name = 'Ренессанс'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Мона Лиза')
),
(
    (SELECT e.exhibition_id FROM exhibition e WHERE e.exhibition_name = 'Ренессанс'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Рождение Венеры')
),
(
    (SELECT e.exhibition_id FROM exhibition e WHERE e.exhibition_name = 'Ренессанс'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Дама с горностаем')
),
(
    (SELECT e.exhibition_id FROM exhibition e WHERE e.exhibition_name = 'Ренессанс'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Сотворение Адама')
),

-- Современное искусство
(
    (SELECT e.exhibition_id FROM exhibition e WHERE e.exhibition_name = 'Современное искусство'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Две Фриды')
),
(
    (SELECT e.exhibition_id FROM exhibition e WHERE e.exhibition_name = 'Современное искусство'),
    (SELECT a.artwork_id FROM artwork a WHERE title = 'Антропометрия голубого периода')
);


-- Добавление тестовых пользователей
INSERT INTO user_data (username, email, user_password) values
('karina', 'karina@mail.ru', '1234'),
('user1', 'user1@example.com', 'user1'), 
('user2', 'user2@example.com', 'user2'), 
('user3', 'user3@example.com', 'user3'), 
('user4', 'user4@example.com', 'user4'); 

-- Добавление администраторов
INSERT INTO admin (login, adminpassword) VALUES
('admin', '1234'), 
('curator', '4321');


-- Добавление произведений в избранное (пример)
INSERT INTO favorite (user_id, artwork_id) VALUES
(1, 1), 
(1, 5), 
(3, 3), 
(4, 2),
(5, 4); 


-- Обновляем существующие записи
UPDATE artwork 
SET description = 
    CASE 
        -- Импрессионизм
        WHEN title = 'Впечатление. Восход солнца' THEN 'Знаковая работа Клода Моне, положившая начало импрессионизму. Изображает гавань Гавра на рассвете.'
        WHEN title = 'Дама в саду Сент-Адресс' THEN 'Картина Клода Моне, изображающая женщину в саду с цветущими розами.'
        WHEN title = 'Пор-Манеш' THEN 'Морской пейзаж Анри Море, написанный в технике пуантилизма.'
        WHEN title = 'Завтрак гребцов' THEN 'Работа Огюста Ренуара, изображающая сцену отдыха на берегу Сены.'
        
        -- Ренессанс
        WHEN title = 'Мона Лиза' THEN 'Легендарный портрет кисти Леонардо да Винчи, известный своей загадочной улыбкой.'
        WHEN title = 'Рождение Венеры' THEN 'Шедевр Сандро Боттичелли, изображающий рождение богини Венеры из морской пены.'
        WHEN title = 'Дама с горностаем' THEN 'Портрет Чечилии Галлерани, фаворитки герцога Миланского, работы Леонардо да Винчи.'
        WHEN title = 'Сотворение Адама' THEN 'Фреска Микеланджело в Сикстинской капелле, изображающая сотворение человека.'
        
        -- Постимпрессионизм
        WHEN title = 'Звездная ночь' THEN 'Сюрреалистичный пейзаж Винсента Ван Гога, написанный в Сен-Реми-де-Прованс.'
        
        -- Фовизм
        WHEN title = 'Танец' THEN 'Яркая работа Анри Матисса, выражающая энергию движения через чистые цвета.'
        
        -- Сюрреализм
        WHEN title = 'Две Фриды' THEN 'Автопортрет Фриды Кало, символизирующий её душевные переживания после развода.'
        
        -- Кубизм
        WHEN title = 'Антропометрия голубого периода' THEN 'Экспериментальная работа Ива Кляйна с использованием синего пигмента и человеческого тела как инструмента.'
    END
WHERE 
    (title, artist_id) IN (
        ('Впечатление. Восход солнца', 1),
        ('Дама в саду Сент-Адресс', 1),
        ('Пор-Манеш', 7),
        ('Завтрак гребцов', 8),
        ('Мона Лиза', 2),
        ('Рождение Венеры', 9),
        ('Дама с горностаем', 2),
        ('Сотворение Адама', 10),
        ('Звездная ночь', 4),
        ('Танец', 6),
        ('Две Фриды', 3),
        ('Антропометрия голубого периода', 11)
);

-- Попытка добавить дубликат стиля
INSERT INTO style (style_name) VALUES ('Импрессионизм'); -- Ошибка: UNIQUE violation

-- Попытка некорректного года
INSERT INTO artwork (title, year, image_path, artist_id, style_id) 
VALUES ('Тест', 999, '/test.jpg', 1, 1); -- Ошибка: CHECK CONSTRAINT



