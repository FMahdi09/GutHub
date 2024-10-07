package guthub.backend.configuration;

public interface JwtConfiguration
{
    String getAccessTokenSecret();

    String getRefreshTokenSecret();

    int getAccessTokenExpiration();

    int getRefreshTokenExpiration();
}
