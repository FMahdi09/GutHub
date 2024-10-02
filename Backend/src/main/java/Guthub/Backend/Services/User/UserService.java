package Guthub.Backend.Services.User;

import Guthub.Backend.Models.UserEntity;

import java.util.List;

public interface UserService
{
    UserEntity createUser(UserEntity user);

    List<UserEntity> getAllUsers();
}
