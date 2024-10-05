package Guthub.Backend.Controllers;

import Guthub.Backend.BaseIntegrationTest;
import Guthub.Backend.Dtos.UserDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        scripts = "/scripts/testRoles.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
public class UserControllerTests extends BaseIntegrationTest
{
    private static final String USER_ENDPOINT = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUsers_givenNoUsers_returnEmptyList()
            throws Exception
    {
        // act
        MvcResult result = mockMvc
                .perform(get(USER_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn();

        // arrange
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
    @Sql(
            scripts = "/scripts/cleanUsers.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getUsers_givenUsers_returnAllUsers()
            throws Exception
    {
        // act
        MvcResult result = mockMvc
                .perform(get(USER_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn();

        // arrange
        String responseBody = result.getResponse().getContentAsString();
        List<UserDto> users = objectMapper.readValue(responseBody, new TypeReference<>()
        {
        });

        Assertions.assertEquals(3, users.size());
    }
}
