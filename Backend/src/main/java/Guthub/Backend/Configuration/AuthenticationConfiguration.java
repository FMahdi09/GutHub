package Guthub.Backend.Configuration;

public interface AuthenticationConfiguration
{
    int getAccessTokenDuration();

    int getRefreshTokenDuration();
}
