package guthub.backend.services.user;

import guthub.backend.models.UserEntity;

import java.util.List;

public interface UserService
{
    UserEntity createUser(UserEntity user);

    List<UserEntity> getAllUsers();
}
