package guthub.backend.services.token.impl;

import guthub.backend.BaseUnitTest;
import guthub.backend.configuration.JwtConfiguration;
import guthub.backend.services.token.exceptions.ExpiredTokenException;
import guthub.backend.services.token.exceptions.InvalidTokenException;
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
    private static final String ACCESS_TOKEN_SECRET = "/XY7T4/5RESFRkjPPzIXOiUHhN4iHfvb8EGKrRVI1qfwOcSdXU0QJigsrfcFuLE5zE3aIJXuX87LWSeUAX8/C+n/USK9Orkd1qDZUS3aVwc/X1caY/nphTsdR1cjBk6Zpn5LUl/3qPf6zTm/ByLpedYe5ywZk6Qy99L5hNPyiMbaYs6IAcKMhQhWhc7+ZLAsT6CjOLczoou9/EzNd7RyuKGQJDsTLRMYcqmRQQ==";

    private static final String REFRESH_TOKEN_SECRET = "4Zxi+xJDyCnSrJFhxwxc4K56W2yEPaQJqYqMOdzKEb8aVL2vTIeiamkUX+fXMRecw95B2X1/7FQ/HzExMzd/sUB5fCtqhSnXE7iwg3pInyc+z7saGubj4hTGpUm+mVXkANCJ7/IjkpPDpWB+i86FCYiuP8GftHW+AenupLzABYGRucwXIOB1fTFDDVzq0j7FEXeWJgGDIP7PSBalSs0jYDB/tC1tthsckkCFZg==";
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

    private static Stream<Arguments> getValidAccessToken()
    {
        return Stream.of(
                Arguments.of(
                        "Subject",
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0Ijo5OTk5OTk5OTk5LCJleHAiOjk5OTk5OTk5OTl9.T5piMTIgPfC6SXuIhVdMzp4tWgiYBGPmDm_xHvEQEoNfYXz0vdOjeExZifhEZ9flnhnYMtlIcbhC8yDwrH78dQ"
                ),
                Arguments.of(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis molestie mauris at tortor pulvinar, vitae facilisis sem finibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sapien tortor, suscipit nec dui sed, elementum vehicula nibh. Donec viverra eleifend sapien, eget pellentesque massa aliquet eget. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin eu nibh eleifend, consequat massa non, fringilla leo. Phasellus sed orci lorem. In lobortis hendrerit neque. Fusce vitae enim ullamcorper, mattis mauris vel, dictum erat. Pellentesque sed metus non velit feugiat interdum quis nec orci. Proin elementum tellus ipsum, sit amet elementum massa dignissim at. Morbi sed placerat massa, porttitor accumsan mi. Duis ut metus vel purus faucibus lacinia sit amet ac enim. Sed justo nisl, finibus eget auctor ac, facilisis sed turpis. Vivamus purus nisl, pharetra sed fringilla et, malesuada at enim. Nunc lorem est, viverra ac sapien vel, vestibulum blandit augue.",
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMb3JlbSBpcHN1bSBkb2xvciBzaXQgYW1ldCwgY29uc2VjdGV0dXIgYWRpcGlzY2luZyBlbGl0LiBEdWlzIG1vbGVzdGllIG1hdXJpcyBhdCB0b3J0b3IgcHVsdmluYXIsIHZpdGFlIGZhY2lsaXNpcyBzZW0gZmluaWJ1cy4gTG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gQWVuZWFuIHNhcGllbiB0b3J0b3IsIHN1c2NpcGl0IG5lYyBkdWkgc2VkLCBlbGVtZW50dW0gdmVoaWN1bGEgbmliaC4gRG9uZWMgdml2ZXJyYSBlbGVpZmVuZCBzYXBpZW4sIGVnZXQgcGVsbGVudGVzcXVlIG1hc3NhIGFsaXF1ZXQgZWdldC4gT3JjaSB2YXJpdXMgbmF0b3F1ZSBwZW5hdGlidXMgZXQgbWFnbmlzIGRpcyBwYXJ0dXJpZW50IG1vbnRlcywgbmFzY2V0dXIgcmlkaWN1bHVzIG11cy4gUHJvaW4gZXUgbmliaCBlbGVpZmVuZCwgY29uc2VxdWF0IG1hc3NhIG5vbiwgZnJpbmdpbGxhIGxlby4gUGhhc2VsbHVzIHNlZCBvcmNpIGxvcmVtLiBJbiBsb2JvcnRpcyBoZW5kcmVyaXQgbmVxdWUuIEZ1c2NlIHZpdGFlIGVuaW0gdWxsYW1jb3JwZXIsIG1hdHRpcyBtYXVyaXMgdmVsLCBkaWN0dW0gZXJhdC4gUGVsbGVudGVzcXVlIHNlZCBtZXR1cyBub24gdmVsaXQgZmV1Z2lhdCBpbnRlcmR1bSBxdWlzIG5lYyBvcmNpLiBQcm9pbiBlbGVtZW50dW0gdGVsbHVzIGlwc3VtLCBzaXQgYW1ldCBlbGVtZW50dW0gbWFzc2EgZGlnbmlzc2ltIGF0LiBNb3JiaSBzZWQgcGxhY2VyYXQgbWFzc2EsIHBvcnR0aXRvciBhY2N1bXNhbiBtaS4gRHVpcyB1dCBtZXR1cyB2ZWwgcHVydXMgZmF1Y2lidXMgbGFjaW5pYSBzaXQgYW1ldCBhYyBlbmltLiBTZWQganVzdG8gbmlzbCwgZmluaWJ1cyBlZ2V0IGF1Y3RvciBhYywgZmFjaWxpc2lzIHNlZCB0dXJwaXMuIFZpdmFtdXMgcHVydXMgbmlzbCwgcGhhcmV0cmEgc2VkIGZyaW5naWxsYSBldCwgbWFsZXN1YWRhIGF0IGVuaW0uIE51bmMgbG9yZW0gZXN0LCB2aXZlcnJhIGFjIHNhcGllbiB2ZWwsIHZlc3RpYnVsdW0gYmxhbmRpdCBhdWd1ZS4iLCJpYXQiOjk5OTk5OTk5OTksImV4cCI6OTk5OTk5OTk5OX0.HI7hQorY-GoV79noHDdjW6Ck5K-RTFV_CYGI6Biz9ZbvTuWBW5RTgvvBlTgXBRvFSlm8T7bnGtpTSy-J0N-YAQ"
                ),
                Arguments.of(
                        null,
                        "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjk5OTk5OTk5OTksImV4cCI6OTk5OTk5OTk5OX0.sFVDSvYdS4nkWGBIc3ZaTUmNZkpfAPnmTvKZcyg0vtaHH4omy1d0-i0jB3PYpJSifmyLrU6MAcZpdowp1wyxqw"
                )
        );
    }

    private static Stream<Arguments> getValidRefreshToken()
    {
        return Stream.of(
                Arguments.of(
                        "Subject",
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0Ijo5OTk5OTk5OTk5LCJleHAiOjk5OTk5OTk5OTl9.ftmXQ9zeB04_fCWqHbiuMR4RldgyOfwCmI6MDqqkRtzHWmaI0ehfnn5sFPTXZ5wDf6COm5oAeKjrqqLnqs7amg"
                ),
                Arguments.of(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis molestie mauris at tortor pulvinar, vitae facilisis sem finibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sapien tortor, suscipit nec dui sed, elementum vehicula nibh. Donec viverra eleifend sapien, eget pellentesque massa aliquet eget. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin eu nibh eleifend, consequat massa non, fringilla leo. Phasellus sed orci lorem. In lobortis hendrerit neque. Fusce vitae enim ullamcorper, mattis mauris vel, dictum erat. Pellentesque sed metus non velit feugiat interdum quis nec orci. Proin elementum tellus ipsum, sit amet elementum massa dignissim at. Morbi sed placerat massa, porttitor accumsan mi. Duis ut metus vel purus faucibus lacinia sit amet ac enim. Sed justo nisl, finibus eget auctor ac, facilisis sed turpis. Vivamus purus nisl, pharetra sed fringilla et, malesuada at enim. Nunc lorem est, viverra ac sapien vel, vestibulum blandit augue.",
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMb3JlbSBpcHN1bSBkb2xvciBzaXQgYW1ldCwgY29uc2VjdGV0dXIgYWRpcGlzY2luZyBlbGl0LiBEdWlzIG1vbGVzdGllIG1hdXJpcyBhdCB0b3J0b3IgcHVsdmluYXIsIHZpdGFlIGZhY2lsaXNpcyBzZW0gZmluaWJ1cy4gTG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gQWVuZWFuIHNhcGllbiB0b3J0b3IsIHN1c2NpcGl0IG5lYyBkdWkgc2VkLCBlbGVtZW50dW0gdmVoaWN1bGEgbmliaC4gRG9uZWMgdml2ZXJyYSBlbGVpZmVuZCBzYXBpZW4sIGVnZXQgcGVsbGVudGVzcXVlIG1hc3NhIGFsaXF1ZXQgZWdldC4gT3JjaSB2YXJpdXMgbmF0b3F1ZSBwZW5hdGlidXMgZXQgbWFnbmlzIGRpcyBwYXJ0dXJpZW50IG1vbnRlcywgbmFzY2V0dXIgcmlkaWN1bHVzIG11cy4gUHJvaW4gZXUgbmliaCBlbGVpZmVuZCwgY29uc2VxdWF0IG1hc3NhIG5vbiwgZnJpbmdpbGxhIGxlby4gUGhhc2VsbHVzIHNlZCBvcmNpIGxvcmVtLiBJbiBsb2JvcnRpcyBoZW5kcmVyaXQgbmVxdWUuIEZ1c2NlIHZpdGFlIGVuaW0gdWxsYW1jb3JwZXIsIG1hdHRpcyBtYXVyaXMgdmVsLCBkaWN0dW0gZXJhdC4gUGVsbGVudGVzcXVlIHNlZCBtZXR1cyBub24gdmVsaXQgZmV1Z2lhdCBpbnRlcmR1bSBxdWlzIG5lYyBvcmNpLiBQcm9pbiBlbGVtZW50dW0gdGVsbHVzIGlwc3VtLCBzaXQgYW1ldCBlbGVtZW50dW0gbWFzc2EgZGlnbmlzc2ltIGF0LiBNb3JiaSBzZWQgcGxhY2VyYXQgbWFzc2EsIHBvcnR0aXRvciBhY2N1bXNhbiBtaS4gRHVpcyB1dCBtZXR1cyB2ZWwgcHVydXMgZmF1Y2lidXMgbGFjaW5pYSBzaXQgYW1ldCBhYyBlbmltLiBTZWQganVzdG8gbmlzbCwgZmluaWJ1cyBlZ2V0IGF1Y3RvciBhYywgZmFjaWxpc2lzIHNlZCB0dXJwaXMuIFZpdmFtdXMgcHVydXMgbmlzbCwgcGhhcmV0cmEgc2VkIGZyaW5naWxsYSBldCwgbWFsZXN1YWRhIGF0IGVuaW0uIE51bmMgbG9yZW0gZXN0LCB2aXZlcnJhIGFjIHNhcGllbiB2ZWwsIHZlc3RpYnVsdW0gYmxhbmRpdCBhdWd1ZS4iLCJpYXQiOjk5OTk5OTk5OTksImV4cCI6OTk5OTk5OTk5OX0.UiMrQYPwLcNny0FaXgPcfwLSH_b-9jYAyU7Xk-VVX5EI-s2ffktI25dCSlhpxy4m1dGQOUM1T6UP5Ibs4bEe1g"
                ),
                Arguments.of(
                        null,
                        "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjk5OTk5OTk5OTksImV4cCI6OTk5OTk5OTk5OX0.H9phJvIo-rk3SKrumfseM_EoiSntdk43eHcDjj4vDcUQG7Ub5lhQFXzpVpm9-suHyTjoGdUMRseFVKgu4-WtJQ"
                )
        );
    }

    private static Stream<Arguments> getExpiredAccessToken()
    {
        return Stream.of(
                Arguments.of("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0Ijo5OTk5OTk5OTk5LCJleHAiOjEwMDB9.t_TW1hXGVa6cbw7aSYj-1ZJzKZObdFc7azLNaptwcr4PIhNPrXDEY8nyRMfSbm_0Eg_WsrGfdQs7f_Jvl75FKw"),
                Arguments.of("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0Ijo5OTk5OTk5OTk5LCJleHAiOjB9.1VtPqisa2N8-TsWZe9rXhkMshxiX9wqBmqynGhdqjpoB3jjWLIk8kOoMKFqHG46r7YkJXd-9LxBoW_1PcZsYxw")
        );
    }

    private static Stream<Arguments> getExpiredRefreshToken()
    {
        return Stream.of(
                Arguments.of("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0Ijo5OTk5OTk5OTk5LCJleHAiOjB9.YhqTH5FI1v3NpC3MmI8M8MN1491yTxGDqfxzfk9xfNFw3kJz_IiHQk9Yq3Dif0hYLmCYWrNn51pHfGKzgbDntg"),
                Arguments.of("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0Ijo5OTk5OTk5OTk5LCJleHAiOjEwMDB9.8RcMsmJLVpgLWMiZEdzsh7j3EelYqTYvZn_RbMcUjOw8BR3tCaDWWdSncZY4rXywFgnVRih-1bhA6I8UOUDaYw")
        );
    }

    private static Stream<Arguments> getInvalidToken()
    {
        return Stream.of(
                Arguments.of("invalid token"),
                Arguments.of("Integer consequat maximus fermentum. Vestibulum nisl ligula, tristique sed mollis quis, fermentum porttitor erat. Fusce varius hendrerit congue. Maecenas semper suscipit ipsum, sed tempor justo pretium vitae. Sed lobortis maximus purus, nec placerat massa faucibus id. In ante nisi, vestibulum et molestie ut, tristique vel leo. Suspendisse est neque, lobortis vel mattis a, rhoncus eu sapien."),
                Arguments.of("EyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTdWJqZWN0IiwiaWF0Ijo5OTk5OTk5OTk5LCJleHAiOjEwMDB9.8RcMsmJLVpgLWMiZEdzsh7j3EelYqTYvZn_RbMcUjOw8BR3tCaDWWdSncZY4rXywFgnVRih-1bhA6I8UOUDaYw")
        );
    }
    //endregion

    @BeforeEach
    void setUp()
    {
        BDDMockito.given(configuration.getAccessTokenSecret())
                .willReturn(ACCESS_TOKEN_SECRET);

        BDDMockito.given(configuration.getRefreshTokenSecret())
                .willReturn(REFRESH_TOKEN_SECRET);

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

    @ParameterizedTest
    @MethodSource("getValidAccessToken")
    void getSubjectFromAccessToken_givenValidToken_getSubject(String expectedSubject,
                                                              String accessToken)
            throws Exception
    {
        // act
        String extractedSubject = jwtTokenService.getSubjectFromAccessToken(accessToken);

        // assert
        Assertions.assertEquals(expectedSubject, extractedSubject);
    }

    @ParameterizedTest
    @MethodSource("getExpiredAccessToken")
    void getSubjectFromAccessToken_givenExpiredToken_throwException(String expiredToken)
    {
        // act & assert
        Assertions.assertThrows(
                ExpiredTokenException.class,
                () -> jwtTokenService.getSubjectFromAccessToken(expiredToken)
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidToken")
    void getSubjectFromAccessToken_givenInvalidToken_throwException(String invalidToken)
    {
        // act & assert
        Assertions.assertThrows(
                InvalidTokenException.class,
                () -> jwtTokenService.getSubjectFromAccessToken(invalidToken)
        );
    }

    @ParameterizedTest
    @MethodSource("getValidRefreshToken")
    void getSubjectFromRefreshToken_givenValidToken_getSubject(String expectedSubject,
                                                               String refreshToken)
            throws Exception
    {
        // act
        String extractedSubject = jwtTokenService.getSubjectFromRefreshToken(refreshToken);

        // assert
        Assertions.assertEquals(expectedSubject, extractedSubject);
    }

    @ParameterizedTest
    @MethodSource("getExpiredRefreshToken")
    void getSubjectFromRefreshToken_givenExpiredToken_throwException(String expiredToken)
    {
        // act & assert
        Assertions.assertThrows(
                ExpiredTokenException.class,
                () -> jwtTokenService.getSubjectFromRefreshToken(expiredToken)
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidToken")
    void getSubjectFromRefreshToken_givenInvalidToken_throwException(String invalidToken)
    {
        // act & assert
        Assertions.assertThrows(
                InvalidTokenException.class,
                () -> jwtTokenService.getSubjectFromRefreshToken(invalidToken)
        );
    }
}
