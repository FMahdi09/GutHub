package Guthub.Backend.Services.User.Impl;

import Guthub.Backend.Models.UserEntity;
import Guthub.Backend.Repositories.UserRepository;
import Guthub.Backend.Services.User.Exceptions.UsernameExistsException;
import Guthub.Backend.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserService implements UserService
{
    private final UserRepository userRepository;

    @Autowired
    public DbUserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserEntity user)
            throws UsernameExistsException
    {
        try
        {
            userRepository.save(user);
        }
        catch (Exception e)
        {
            throw new UsernameExistsException();
        }
    }

    @Override
    public List<UserEntity> getAllUsers()
    {
        return userRepository.findAll();
    }
}
