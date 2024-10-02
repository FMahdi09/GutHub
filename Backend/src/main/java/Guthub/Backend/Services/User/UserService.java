package Guthub.Backend.Services.User;

import Guthub.Backend.Models.UserEntity;

import java.util.List;

public interface UserService
{
    List<UserEntity> getAllUsers();
}
