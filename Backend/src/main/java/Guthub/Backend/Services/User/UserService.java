package Guthub.Backend.Services.User;

import Guthub.Backend.Models.UserEntity;
import Guthub.Backend.Services.User.Exceptions.UsernameExistsException;

import java.util.List;

public interface UserService
{
    void createUser(UserEntity user)
            throws UsernameExistsException;

    List<UserEntity> getAllUsers();
}
