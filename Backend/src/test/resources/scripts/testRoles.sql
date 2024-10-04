INSERT INTO roles
    (name)
VALUES ('ADMIN'),
       ('USER')
ON CONFLICT DO NOTHING;