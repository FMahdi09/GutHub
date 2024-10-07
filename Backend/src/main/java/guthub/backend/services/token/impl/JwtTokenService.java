package guthub.backend.services.token.impl;

import guthub.backend.configuration.JwtConfiguration;
import guthub.backend.services.token.TokenPair;
import guthub.backend.services.token.TokenService;
import guthub.backend.services.token.exceptions.ExpiredTokenException;
import guthub.backend.services.token.exceptions.InvalidTokenException;
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

    private final int accessTokenExpiration;

    private final int refreshTokenExpiration;

    @Autowired
    public JwtTokenService(JwtConfiguration config)
    {
        this.accessTokenSecret = getSigningKey(config.getAccessTokenSecret());
        this.refreshTokenSecret = getSigningKey(config.getRefreshTokenSecret());

        this.accessTokenExpiration = config.getAccessTokenExpiration();
        this.refreshTokenExpiration = config.getRefreshTokenExpiration();
    }

    @Override
    public TokenPair generateTokenPair(String subject, Date issuedAt)
    {
        JwtBuilder tokenBase = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt);

        String accessToken = tokenBase
                .setExpiration(new Date(issuedAt.getTime() + accessTokenExpiration))
                .signWith(accessTokenSecret, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = tokenBase
                .setExpiration(new Date(issuedAt.getTime() + refreshTokenExpiration))
                .signWith(refreshTokenSecret, SignatureAlgorithm.HS512)
                .compact();

        return new TokenPair(accessToken, refreshToken);
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
