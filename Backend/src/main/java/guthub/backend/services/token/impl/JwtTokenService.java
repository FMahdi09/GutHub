package guthub.backend.services.token.impl;

import guthub.backend.configuration.JwtConfiguration;
import guthub.backend.services.token.exceptions.ExpiredTokenException;
import guthub.backend.services.token.exceptions.InvalidTokenException;
import guthub.backend.services.token.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

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

    @Override
    public String getSubjectFromAccessToken(String accessToken)
            throws InvalidTokenException, ExpiredTokenException
    {
        return extractClaim(accessToken, accessTokenSecret, Claims::getSubject);
    }

    @Override
    public String getSubjectFromRefreshToken(String refreshToken)
            throws InvalidTokenException, ExpiredTokenException
    {
        return extractClaim(refreshToken, refreshTokenSecret, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Key key, Function<Claims, T> claimsResolver)
            throws ExpiredTokenException, InvalidTokenException
    {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, Key key)
            throws ExpiredTokenException, InvalidTokenException
    {
        try
        {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (ExpiredJwtException ex)
        {
            throw new ExpiredTokenException(ex.getMessage());
        }
        catch (JwtException ex)
        {
            throw new InvalidTokenException(ex.getMessage());
        }
    }

    private Key getSigningKey(String base64Secret)
    {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
