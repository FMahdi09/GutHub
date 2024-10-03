package Guthub.Backend.Services.Token.Impl;

import Guthub.Backend.Configuration.JwtConfiguration;
import Guthub.Backend.Services.Token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenService implements TokenService
{
    private final String accessTokenSecret;

    private final String refreshTokenSecret;

    @Autowired
    public JwtTokenService(JwtConfiguration config)
    {
        this.accessTokenSecret = config.getAccessTokenSecret();
        this.refreshTokenSecret = config.getRefreshTokenSecret();
    }

    @Override
    public String generateAccessToken(String subject, Date issuedAt, Date expiresAt)
    {
        return "";
    }

    @Override
    public String generateRefreshToken(String subject, Date issuedAt, Date expiresAt)
    {
        return "";
    }
}
