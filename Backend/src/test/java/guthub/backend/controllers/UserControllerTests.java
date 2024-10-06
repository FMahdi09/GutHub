package guthub.backend.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import guthub.backend.BaseIntegrationTest;
import guthub.backend.dtos.DetailedUserDto;
import guthub.backend.dtos.RegisterDto;
import guthub.backend.dtos.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        scripts = "/scripts/testRoles.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = "/scripts/cleanUsers.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class UserControllerTests extends BaseIntegrationTest
{
    private static final String USER_ENDPOINT = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //region <test data>
    private static Stream<Arguments> getValidRegisterData()
    {
        return Stream.of(
                Arguments.of(
                        new RegisterDto("username", "password", "email@mail.com")
                ),
                Arguments.of(
                        new RegisterDto("Ghanima", "SpoilOfWar", "ghani@mail.com")
                ),
                Arguments.of(
                        new RegisterDto("Leto ||", "HwiNoree", "hwi@mail.com")
                )
        );
    }

    private static Stream<Arguments> getInvalidRegisterData()
    {
        return Stream.of(
                Arguments.of(
                        new RegisterDto("a", "password", "email@mail.com")
                ),
                Arguments.of(
                        new RegisterDto("username", "waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaayTooLong", "email@mail.com")
                ),
                Arguments.of(
                        new RegisterDto("username", "password", "not an email")
                ),
                Arguments.of(
                        new RegisterDto("", "", "")
                ),
                Arguments.of(
                        new RegisterDto(null, null, null)
                )
        );
    }
    //endregion

    @Test
    void getUsers_givenNoUsers_returnEmptyList()
            throws Exception
    {
        // act
        MvcResult result = mockMvc
                .perform(get(USER_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        String responseBody = result.getResponse().getContentAsString();
        List<UserDto> users = objectMapper.readValue(responseBody, new TypeReference<>()
        {
        });

        Assertions.assertEquals(0, users.size());
    }

    @Test
    @Sql(
            scripts = "/scripts/testUsers.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void getUsers_givenUsers_returnAllUsers()
            throws Exception
    {
        // act
        MvcResult result = mockMvc
                .perform(get(USER_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        String responseBody = result.getResponse().getContentAsString();
        List<UserDto> users = objectMapper.readValue(responseBody, new TypeReference<>()
        {
        });

        Assertions.assertEquals(3, users.size());
    }

    @ParameterizedTest
    @MethodSource("getValidRegisterData")
    void createUser_givenValidData_returnCreatedUser(RegisterDto registerDto)
            throws Exception
    {
        // arrange
        String requestBody = objectMapper.writeValueAsString(registerDto);

        // act
        MvcResult result = mockMvc
                .perform(post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        String responseBody = result.getResponse().getContentAsString();
        DetailedUserDto createdUser = objectMapper.readValue(responseBody, DetailedUserDto.class);

        Assertions.assertEquals(createdUser.getUsername(), registerDto.getUsername());
        Assertions.assertEquals(createdUser.getEmail(), registerDto.getEmail());
    }

    @ParameterizedTest
    @MethodSource("getValidRegisterData")
    void createUser_givenTakenUsername_returnConflict(RegisterDto registerDto)
            throws Exception
    {
        // arrange
        String requestBody = objectMapper.writeValueAsString(registerDto);

        // act
        mockMvc.perform(post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // assert
                .andExpect(status().isConflict());
    }

    @ParameterizedTest
    @MethodSource("getInvalidRegisterData")
    void createUser_givenInvalidData_returnBadRequest(RegisterDto registerDto)
            throws Exception
    {
        // arrange
        String requestBody = objectMapper.writeValueAsString(registerDto);

        // act
        mockMvc.perform(post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // assert
                .andExpect(status().isBadRequest());
    }
}
