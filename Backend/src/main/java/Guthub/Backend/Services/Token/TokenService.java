package Guthub.Backend.Services.Token;

import Guthub.Backend.Services.Token.Exceptions.ExpiredTokenException;
import Guthub.Backend.Services.Token.Exceptions.InvalidTokenException;

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
