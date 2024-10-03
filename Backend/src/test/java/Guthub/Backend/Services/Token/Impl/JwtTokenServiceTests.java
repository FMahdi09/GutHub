package Guthub.Backend.Services.Token.Impl;

import Guthub.Backend.BaseUnitTest;
import Guthub.Backend.Configuration.JwtConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;

import java.util.Date;
import java.util.stream.Stream;

public class JwtTokenServiceTests extends BaseUnitTest
{
    //region <testData>
    private static final String accessTokenSecret = "/XY7T4/5RESFRkjPPzIXOiUHhN4iHfvb8EGKrRVI1qfwOcSdXU0QJigsrfcFuLE5zE3aIJXuX87LWSeUAX8/C+n/USK9Orkd1qDZUS3aVwc/X1caY/nphTsdR1cjBk6Zpn5LUl/3qPf6zTm/ByLpedYe5ywZk6Qy99L5hNPyiMbaYs6IAcKMhQhWhc7+ZLAsT6CjOLczoou9/EzNd7RyuKGQJDsTLRMYcqmRQQ==";

    private static final String refreshTokenSecret = "4Zxi+xJDyCnSrJFhxwxc4K56W2yEPaQJqYqMOdzKEb8aVL2vTIeiamkUX+fXMRecw95B2X1/7FQ/HzExMzd/sUB5fCtqhSnXE7iwg3pInyc+z7saGubj4hTGpUm+mVXkANCJ7/IjkpPDpWB+i86FCYiuP8GftHW+AenupLzABYGRucwXIOB1fTFDDVzq0j7FEXeWJgGDIP7PSBalSs0jYDB/tC1tthsckkCFZg==";
    //endregion

    @Mock
    private JwtConfiguration configuration;

    private JwtTokenService jwtTokenService;

    //region <testFunctions>
    private static Stream<Arguments> getValidAccessTokenData()
    {
        return Stream.of(
                Arguments.of(
                        "Subject",
                        new Date(1727913600L * 1000),
                        new Date(1728000000L * 1000),
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0IjoxNzI3OTEzNjAwLCJleHAiOjE3MjgwMDAwMDB9.eUaZ-DR_Bi2_vbI_lRBeBtn8Fof83Wf8a2wS6qNeb-uXGa9INEYPvDZU_4TeO5WhTEW5MJKAVDF4eMaC6t8yYw"
                ),
                Arguments.of(
                        "my very very important subject",
                        new Date(1827913600L * 1000),
                        new Date(1828000000L * 1000),
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJteSB2ZXJ5IHZlcnkgaW1wb3J0YW50IHN1YmplY3QiLCJpYXQiOjE4Mjc5MTM2MDAsImV4cCI6MTgyODAwMDAwMH0.BT3azCLPt5UywgpQr1waA6wQWx3TVfEEp78Lu_0VRLR_JxHeW4WNMNqzZcwh9pVYcEPEnh0_LCH4bNr2ugWEnA"
                ),
                Arguments.of(
                        "",
                        new Date(0),
                        new Date(0),
                        "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjAsImV4cCI6MH0.Gv6ZKhR47BNKFhHp2vH0We65LIjwWfyZ23nw2D94rk6R7umRY8SZ41vMy8eUgsHHeV6myKNEdsbFQHMzj_uyCQ"
                )
        );
    }

    private static Stream<Arguments> getValidRefreshTokenData()
    {
        return Stream.of(
                Arguments.of(
                        "Subject",
                        new Date(1727913600L * 1000),
                        new Date(1728000000L * 1000),
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0IjoxNzI3OTEzNjAwLCJleHAiOjE3MjgwMDAwMDB9.ZqimbkGrX19L9uVDDV9FxqDzTdMDdNizI3I6_aGAbEkgTryJ709G1GG80Je6n_WcZ0yuedBRvm1rQwdhaUbPyg"
                ),
                Arguments.of(
                        "my very very important subject",
                        new Date(1827913600L * 1000),
                        new Date(1828000000L * 1000),
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJteSB2ZXJ5IHZlcnkgaW1wb3J0YW50IHN1YmplY3QiLCJpYXQiOjE4Mjc5MTM2MDAsImV4cCI6MTgyODAwMDAwMH0.T_9XdD0MBD3wkHd-6wh54KvvCOk7UHepU97MCCTZ4HsTGK7V4rT7IFuflRFIUxBnA3vb0qODCY6GQq0GrcnvCA"
                ),
                Arguments.of(
                        "",
                        new Date(0),
                        new Date(0),
                        "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjAsImV4cCI6MH0.Bfz2P8aqxLrvoUrx9QCu4GuRfSjoYJJzFgTaJJDDsyvVAHUeLqeMmpVwaNpij8mrjo3B61h4k305VegRyDHSNQ"
                )
        );
    }
    //endregion

    @BeforeEach
    void setUp()
    {
        BDDMockito.given(configuration.getAccessTokenSecret())
                .willReturn(accessTokenSecret);

        BDDMockito.given(configuration.getRefreshTokenSecret())
                .willReturn(refreshTokenSecret);

        jwtTokenService = new JwtTokenService(configuration);
    }

    @ParameterizedTest
    @MethodSource("getValidAccessTokenData")
    void generateAccessToken_givenValidData_generateToken(String subject,
                                                          Date issuedAt,
                                                          Date expiresAt,
                                                          String expectedToken)
    {
        // act
        String createdToken = jwtTokenService.generateAccessToken(subject, issuedAt, expiresAt);

        // assert
        Assertions.assertEquals(expectedToken, createdToken);
    }

    @ParameterizedTest
    @MethodSource("getValidRefreshTokenData")
    void generateRefreshToken_givenValidData_generateToken(String subject,
                                                           Date issuedAt,
                                                           Date expiresAt,
                                                           String expectedToken)
    {
        // act
        String createdToken = jwtTokenService.generateRefreshToken(subject, issuedAt, expiresAt);

        // assert
        Assertions.assertEquals(expectedToken, createdToken);
    }
}
