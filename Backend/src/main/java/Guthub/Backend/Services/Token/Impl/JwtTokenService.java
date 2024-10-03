package Guthub.Backend.Services.Token.Impl;

import Guthub.Backend.Configuration.JwtConfiguration;
import Guthub.Backend.Services.Token.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenService implements TokenService
{
    private final Key accessTokenSecret;

    private final Key refreshTokenSecret;

    @Autowired
    public JwtTokenService(JwtConfiguration config)
    {
        this.accessTokenSecret = getSigningKey(config.getAccessTokenSecret());
        this.refreshTokenSecret = getSigningKey(config.getRefreshTokenSecret());
    }

    @Override
    public String generateAccessToken(String subject, Date issuedAt, Date expiresAt)
    {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(accessTokenSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generateRefreshToken(String subject, Date issuedAt, Date expiresAt)
    {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(refreshTokenSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSigningKey(String base64Secret)
    {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
