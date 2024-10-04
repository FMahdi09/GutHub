INSERT INTO users
    (username, password, email)
VALUES ('Sol Weintraub',
        '$2a$10$BXsPCGLlzmaTTNzL/WbySOORdjWs5O6CItiK7VrYmJUitndV/ES9C', -- Barnard
        'sol@mail.com'),
       ('Lenar Hoyt',
        '$2a$10$jqbtIpHVrmiAp1Cstzh0SeS8jSU5peH4MoDvn8KFkT0.qm27Nor22', -- Armaghast
        'hoyt@mail.com'),
       ('Martin Silenus',
        '$2a$10$TSFCyxsNEuyZY9DnN3CIGOgbksx3rTKJ2V4MUxWjJzAWQTFDYVugO', -- Prophet
        'silenus@mail.com');

INSERT INTO users_roles
    (user_id, role_id)
VALUES ((SELECT id
         FROM users
         WHERE username like 'Sol Weintraub'),
        (SELECT id
         FROM roles
         WHERE name like 'USER')),
       ((SELECT id
         FROM users
         WHERE username like 'Lenar Hoyt'),
        (SELECT id
         FROM roles
         WHERE name like 'USER')),
       ((SELECT id
         FROM users
         WHERE username like 'Martin Silenus'),
        (SELECT id
         FROM roles
         WHERE name like 'USER'));