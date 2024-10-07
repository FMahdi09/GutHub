package guthub.backend.services.authentication;

import guthub.backend.services.token.TokenPair;

public interface AuthenticationService
{
    TokenPair login(String username, String password);

    TokenPair refresh(String refreshToken);
}
