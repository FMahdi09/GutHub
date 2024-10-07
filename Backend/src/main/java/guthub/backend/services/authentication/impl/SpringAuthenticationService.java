package guthub.backend.services.authentication.impl;

import guthub.backend.services.authentication.AuthenticationService;
import guthub.backend.services.authentication.exceptions.InvalidRefreshTokenException;
import guthub.backend.services.token.TokenPair;
import guthub.backend.services.token.TokenService;
import guthub.backend.services.token.exceptions.ExpiredTokenException;
import guthub.backend.services.token.exceptions.InvalidTokenException;
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

    @Autowired
    public SpringAuthenticationService(AuthenticationManager authenticationManager,
                                       TokenService tokenService)
    {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
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

        return tokenService.generateTokenPair(authentication.getName(), issuedAt);
    }

    @Override
    public TokenPair refresh(String refreshToken)
    {
        try
        {
            String username = tokenService.getSubjectFromRefreshToken(refreshToken);

            Date issuedAt = new Date();

            return tokenService.generateTokenPair(username, issuedAt);
        }
        catch (InvalidTokenException | ExpiredTokenException ex)
        {
            throw new InvalidRefreshTokenException(ex.getMessage());
        }
    }
}
