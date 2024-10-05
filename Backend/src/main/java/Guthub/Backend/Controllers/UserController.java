package Guthub.Backend.Controllers;

import Guthub.Backend.Dtos.DetailedUserDto;
import Guthub.Backend.Dtos.RegisterDto;
import Guthub.Backend.Dtos.UserDto;
import Guthub.Backend.Mappers.UserMapper;
import Guthub.Backend.Models.UserEntity;
import Guthub.Backend.Services.User.UserService;
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
    public DetailedUserDto createUser(@RequestBody RegisterDto registerDto)
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
