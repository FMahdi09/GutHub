INSERT INTO users
    (id, username, password, email)
VALUES (0,
        'Sol Weintraub',
        '$2a$10$wOGmrL9yxtNnHYC7F6ZMkut5av2a66/BeOm62se3oKQ1ueAdufadm', -- BarnardsStar
        'sol@mail.com'),
       (1,
        'Lenar Hoyt',
        '$2a$10$jqbtIpHVrmiAp1Cstzh0SeS8jSU5peH4MoDvn8KFkT0.qm27Nor22', -- Armaghast
        'hoyt@mail.com'),
       (2,
        'Martin Silenus',
        '$2a$10$/BzVb.ljBEx.o0mwAfoqbOGD0OpjdCojXb.c/p0PP1.r1rv9/x8BO', -- TheProphet
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