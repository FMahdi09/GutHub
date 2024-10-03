package Guthub.Backend.Services.Authentication.Impl;

import Guthub.Backend.Configuration.AuthenticationConfiguration;
import Guthub.Backend.Dtos.TokenDto;
import Guthub.Backend.Services.Authentication.AuthenticationService;
import Guthub.Backend.Services.Token.TokenPair;
import Guthub.Backend.Services.Token.TokenService;
import Guthub.Backend.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SpringAuthenticationService implements AuthenticationService
{
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final TokenService tokenService;

    private final int accessTokenDuration;

    private final int refreshTokenDuration;

    @Autowired
    public SpringAuthenticationService(AuthenticationManager authenticationManager,
                                       UserService userService,
                                       TokenService tokenService,
                                       AuthenticationConfiguration authenticationConfiguration)
    {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;

        this.accessTokenDuration = authenticationConfiguration.getAccessTokenDuration();
        this.refreshTokenDuration = authenticationConfiguration.getRefreshTokenDuration();
    }

    @Override
    public TokenPair login(String username, String password)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        Date issuedAt = new Date();
        Date accessExpiration = new Date(issuedAt.getTime() + accessTokenDuration);
        Date refreshExpiration = new Date(issuedAt.getTime() + refreshTokenDuration);

        String accessToken = tokenService.generateAccessToken(
                authentication.getName(),
                issuedAt,
                accessExpiration
        );

        String refreshToken = tokenService.generateRefreshToken(
                authentication.getName(),
                issuedAt,
                refreshExpiration
        );

        return new TokenPair(accessToken, refreshToken);
    }
}
