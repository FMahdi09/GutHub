package guthub.backend.controllers;

import guthub.backend.dtos.DetailedUserDto;
import guthub.backend.dtos.RegisterDto;
import guthub.backend.dtos.UserDto;
import guthub.backend.mappers.UserMapper;
import guthub.backend.models.UserEntity;
import guthub.backend.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController
{
    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService,
                          UserMapper userMapper)
    {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public DetailedUserDto createUser(@Valid @RequestBody RegisterDto registerDto)
    {
        UserEntity user = userMapper.fromRegisterDto(registerDto);

        UserEntity createdUser = userService.createUser(user);

        return userMapper.toDetailedUserDto(createdUser);
    }

    @GetMapping
    public List<UserDto> getUsers()
    {
        List<UserEntity> entities = userService.getAllUsers();

        return userMapper.toUserDtoList(entities);
    }
}
