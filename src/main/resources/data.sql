-- Smažeme pro jistotu vše, pokud by tam něco zbylo (při ddl-auto=create-drop není nutné, ale jistota)
-- INSERT pro uživatele
INSERT INTO users (id, username, email)
VALUES ('a67', 'Sup Dojizdak', 'sup@dojizdak.cz');
