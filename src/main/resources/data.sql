-- 1. Vyčištění
DELETE FROM instruments;
DELETE FROM users;

-- 2. Uživatelé s validními UUID
-- Sup Dojizdak
INSERT INTO users (id, username, email, role)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Sup Dojizdak', 'sup@dojizdak.cz', 'ADMIN');

-- Jan Novak
INSERT INTO users (id, username, email, role)
VALUES ('67e55044-10b1-426f-9247-bb680e5fe0c8', 'Jan Novak', 'jan.novak@seznam.cz', 'USER');

-- 3. Nástroje
-- Pro Supa Dojizdaka (odkazuje na 550e8400...)
INSERT INTO instruments (id, name, price, user_id)
VALUES (UUID(), 'Fender Stratocaster 1964', 85000.0, '550e8400-e29b-41d4-a716-446655440000');

INSERT INTO instruments (id, name, price, user_id)
VALUES (UUID(), 'Gibson Thunderbird Bass', 42000.5, '550e8400-e29b-41d4-a716-446655440000');

-- Pro Jana Nováka (odkazuje na 67e55044...)
INSERT INTO instruments (id, name, price, user_id)
VALUES (UUID(), 'Yamaha P-45 Digital Piano', 12500.0, '67e55044-10b1-426f-9247-bb680e5fe0c8');