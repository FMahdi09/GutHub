package Guthub.Backend.Services.Authentication;

import Guthub.Backend.Dtos.TokenDto;
import Guthub.Backend.Services.Token.TokenPair;

public interface AuthenticationService
{
    TokenPair login(String username, String password);
}
