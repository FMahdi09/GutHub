package Guthub.Backend.Services.Token;

import java.util.Date;

public interface TokenService
{
    String generateAccessToken(String subject, Date issuedAt, Date expiresAt);

    String generateRefreshToken(String subject, Date issuedAt, Date expiresAt);
}
