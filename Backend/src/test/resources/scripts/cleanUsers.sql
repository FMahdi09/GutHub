-- unassign all roles
DELETE
FROM users_roles
WHERE TRUE;

-- delete all users
DELETE
FROM users
WHERE TRUE;