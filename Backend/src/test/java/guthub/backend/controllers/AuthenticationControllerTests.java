package guthub.backend.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import guthub.backend.BaseIntegrationTest;
import guthub.backend.dtos.LoginDto;
import guthub.backend.dtos.TokenDto;
import guthub.backend.services.token.TokenService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
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

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        scripts = {"/scripts/testRoles.sql", "/scripts/testUsers.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = "/scripts/cleanUsers.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
class AuthenticationControllerTests extends BaseIntegrationTest
{
    private static final String LOGIN_ENDPOINT = "/auth/login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    //region <test data>
    private static Stream<Arguments> getValidCredentials()
    {
        return Stream.of(
                Arguments.of(
                        new LoginDto("Sol Weintraub", "BarnardsStar")
                ),
                Arguments.of(
                        new LoginDto("Lenar Hoyt", "Armaghast")
                ),
                Arguments.of(
                        new LoginDto("Martin Silenus", "TheProphet")
                )
        );
    }

    private static Stream<Arguments> getInvalidCredentials()
    {
        return Stream.of(
                Arguments.of(
                        new LoginDto("Farad'n", "CorrinoPrince")
                ),
                Arguments.of(
                        new LoginDto("Tyekanik", "TheBashar")
                )
        );
    }
    //endregion

    @ParameterizedTest
    @MethodSource("getValidCredentials")
    void login_givenValidCredentials_returnAccessToken(LoginDto loginDto)
            throws Exception
    {
        // arrange
        String requestBody = objectMapper.writeValueAsString(loginDto);

        // act
        MvcResult result = mockMvc
                .perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        String responseBody = result.getResponse().getContentAsString();
        TokenDto tokenDto = objectMapper.readValue(responseBody, TokenDto.class);
        String subject = tokenService.getSubjectFromAccessToken(tokenDto.getAccessToken());

        Assertions.assertEquals(loginDto.getUsername(), subject);
    }

    @ParameterizedTest
    @MethodSource("getValidCredentials")
    void login_givenValidCredentials_returnRefreshTokenCookie(LoginDto loginDto)
            throws Exception
    {
        // arrange
        String requestBody = objectMapper.writeValueAsString(loginDto);

        // act
        MvcResult result = mockMvc
                .perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        Cookie refreshCookie = result.getResponse().getCookie("refreshToken");

        Assertions.assertNotNull(refreshCookie);

        String refreshToken = refreshCookie.getValue();
        String subject = tokenService.getSubjectFromRefreshToken(refreshToken);

        Assertions.assertEquals(loginDto.getUsername(), subject);
    }

    @ParameterizedTest
    @MethodSource("getInvalidCredentials")
    void login_givenInvalidCredentials_returnForbidden(LoginDto loginDto)
            throws Exception
    {
        // arrange
        String requestBody = objectMapper.writeValueAsString(loginDto);

        // act
        mockMvc
                .perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // assert
                .andExpect(status().isForbidden());
    }


}
