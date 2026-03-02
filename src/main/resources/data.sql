-- 1. Vyčištění
DELETE FROM instruments;
DELETE FROM users;

-- 2. Uživatelé
INSERT INTO users (id, username, email, role) VALUES
                                                  ('550e8400-e29b-41d4-a716-446655440000', 'Sup Dojizdak', 'sup@dojizdak.cz', 'ADMIN'),
                                                  ('67e55044-10b1-426f-9247-bb680e5fe0c8', 'Jan Novak', 'jan.novak@seznam.cz', 'USER'),
                                                  ('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d', 'Marie Sladka', 'marie@hudba.cz', 'EDITOR'),
                                                  ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Karel Kryl', 'karel@kryl.cz', 'USER'),
                                                  ('98765432-1234-abcd-beef-567890abcdef', 'Petra Brnkajici', 'petra@kytary.cz', 'USER'),
                                                  ('12345678-1234-1234-1234-1234567890ab', 'Ludvig van B.', 'ludvig@classic.de', 'ADMIN');

-- 3. Nástroje
-- Sup Dojizdak
INSERT INTO instruments (id, name, price, description, user_id) VALUES
                                                                    (UUID(), 'Fender Stratocaster 1964', 85000.0, 'Původní stav, lehce ohraný hmatník.', '550e8400-e29b-41d4-a716-446655440000'),
                                                                    (UUID(), 'Gibson Thunderbird Bass', 42000.5, 'Baskytara s pořádným rockovým zvukem.', '550e8400-e29b-41d4-a716-446655440000'),
                                                                    (UUID(), 'Marshall JCM800', 35000.0, 'Legendární lampový zesilovač.', '550e8400-e29b-41d4-a716-446655440000');

-- Jan Novak
INSERT INTO instruments (id, name, price, description, user_id) VALUES
                                                                    (UUID(), 'Yamaha P-45 Digital Piano', 12500.0, 'Ideální pro cvičení v paneláku.', '67e55044-10b1-426f-9247-bb680e5fe0c8'),
                                                                    (UUID(), 'Stojan na noty Millenium', 450.0, 'Skládací, černý.', '67e55044-10b1-426f-9247-bb680e5fe0c8');

-- Marie Sladka
INSERT INTO instruments (id, name, price, description, user_id) VALUES
                                                                    (UUID(), 'Hohner Marine Band Deluxe', 1200.0, 'Foukací harmonika v ladění C.', 'a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d'),
                                                                    (UUID(), 'Ukulele Concert', 2800.0, 'Dřevo mahagon, krásný zvuk.', 'a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d'),
                                                                    (UUID(), 'Akordeon Delicia', 18500.0, '72 basů, po celkové opravě.', 'a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d');

-- Karel Kryl
INSERT INTO instruments (id, name, price, description, user_id) VALUES
    (UUID(), 'Španělka Cremona', 3200.0, 'Klasická kytara s nylonovými strunami.', 'f47ac10b-58cc-4372-a567-0e02b2c3d479');

-- Petra Brnkajici
INSERT INTO instruments (id, name, price, description, user_id) VALUES
                                                                    (UUID(), 'Ibanez RG421', 9800.0, 'Rychlý krk, super pro metal.', '98765432-1234-abcd-beef-567890abcdef'),
                                                                    (UUID(), 'Boss Katana 50', 5600.0, 'Modelovací kombo s efekty.', '98765432-1234-abcd-beef-567890abcdef');

-- Ludvig van B.
INSERT INTO instruments (id, name, price, description, user_id) VALUES
    (UUID(), 'Steinway & Sons Model D', 2500000.0, 'Koncertní křídlo, špičkový stav.', '12345678-1234-1234-1234-1234567890ab');

INSERT INTO instruments (id, name, price, description, user_id)
VALUES ('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d', 'Test Guitar', 1000.0, 'Desc', '550e8400-e29b-41d4-a716-446655440000');