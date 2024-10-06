package guthub.backend.services.token;

import guthub.backend.services.token.exceptions.ExpiredTokenException;
import guthub.backend.services.token.exceptions.InvalidTokenException;

import java.util.Date;

public interface TokenService
{
    String generateAccessToken(String subject, Date issuedAt, Date expiresAt);

    String generateRefreshToken(String subject, Date issuedAt, Date expiresAt);

    String getSubjectFromAccessToken(String accessToken)
            throws InvalidTokenException, ExpiredTokenException;

    String getSubjectFromRefreshToken(String refreshToken)
            throws InvalidTokenException, ExpiredTokenException;
}
