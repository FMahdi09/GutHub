package guthub.backend.services.user.impl;

import guthub.backend.models.Role;
import guthub.backend.models.UserEntity;
import guthub.backend.repositories.RoleRepository;
import guthub.backend.repositories.UserRepository;
import guthub.backend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserService implements UserService
{
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DbUserService(UserRepository userRepository,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(UserEntity user)
    {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Role userRole = roleRepository.findByName("USER").orElseThrow();
        user.setRoles(List.of(userRole));

        userRepository.save(user);

        return user;
    }

    @Override
    public List<UserEntity> getAllUsers()
    {
        return userRepository.findAll();
    }
}
