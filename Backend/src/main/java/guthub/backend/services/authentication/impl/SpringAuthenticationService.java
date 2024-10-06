package guthub.backend.services.authentication.impl;

import guthub.backend.configuration.AuthenticationConfiguration;
import guthub.backend.services.authentication.AuthenticationService;
import guthub.backend.services.token.TokenPair;
import guthub.backend.services.token.TokenService;
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

    private final TokenService tokenService;

    private final int accessTokenDuration;

    private final int refreshTokenDuration;

    @Autowired
    public SpringAuthenticationService(AuthenticationManager authenticationManager,
                                       TokenService tokenService,
                                       AuthenticationConfiguration authenticationConfiguration)
    {
        this.authenticationManager = authenticationManager;
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
