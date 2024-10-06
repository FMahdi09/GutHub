package guthub.backend.configuration;

public interface AuthenticationConfiguration
{
    int getAccessTokenDuration();

    int getRefreshTokenDuration();
}
