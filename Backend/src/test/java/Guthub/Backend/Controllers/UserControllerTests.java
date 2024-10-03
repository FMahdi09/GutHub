package Guthub.Backend.Controllers;

import Guthub.Backend.BaseIntegrationTest;
import Guthub.Backend.Dtos.RegisterDto;
import Guthub.Backend.Dtos.UserDto;
import Guthub.Backend.Models.UserEntity;
import Guthub.Backend.Repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class UserControllerTests extends BaseIntegrationTest
{
    //region <testData>
    private static final UserEntity tick = new UserEntity(
            "Tick",
            "change",
            "duck@gmail.com",
            new ArrayList<>()
    );
    private static final UserEntity trick = new UserEntity(
            "Trick",
            "me",
            "duck@gmail.com",
            new ArrayList<>()
    );
    private static final UserEntity track = new UserEntity(
            "Track",
            "please",
            "duck@gmail.com",
            new ArrayList<>()
    );
    //endregion

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> getValidUserData()
    {
        return Stream.of(
                Arguments.of("username", "changeMe", "email"),
                Arguments.of("donald duck", "superSecret", "email"),
                Arguments.of("dagobert duck", "money", "email")
        );
    }

    private static Stream<Arguments> getTestUserLists()
    {
        return Stream.of(
                Arguments.of(List.of(trick)),
                Arguments.of(Arrays.asList(trick, tick, track))
        );
    }

    @AfterEach
    void tearDown()
    {
        userRepository.deleteAll();
    }

    @Test
    void getUsers_givenNoUsers_returnNothing()
    {
        // act
        List<UserDto> users = userController.getUsers();

        // assert
        Assertions.assertTrue(users.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("getTestUserLists")
    void getUsers_givenUsers_returnAllUsers(List<UserEntity> givenUsers)
    {
        // arrange
        userRepository.saveAll(givenUsers);

        // act
        List<UserDto> retrievedUsers = userController.getUsers();

        // assert
        Assertions.assertEquals(givenUsers.size(), retrievedUsers.size());
    }

    @ParameterizedTest
    @MethodSource("getValidUserData")
    void createUser_givenValidData_createUser(String username,
                                              String password,
                                              String email)
    {
        // arrange
        RegisterDto toCreate = new RegisterDto();
        toCreate.setUsername(username);
        toCreate.setPassword(password);
        toCreate.setEmail(email);

        // act
        UserDto createdUser = userController.createUser(toCreate);

        // assert
        Assertions.assertEquals(username, createdUser.getUsername());
        Assertions.assertEquals(email, createdUser.getEmail());
    }

    @ParameterizedTest
    @MethodSource("getValidUserData")
    void createUser_givenExistingUsername_throwException(String username,
                                                         String password,
                                                         String email)
    {
        // arrange
        RegisterDto toCreate = new RegisterDto();
        toCreate.setUsername(username);
        toCreate.setPassword(password);
        toCreate.setEmail(email);

        // act & assert
        userController.createUser(toCreate);

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> userController.createUser(toCreate)
        );
    }
}
