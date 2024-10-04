package Guthub.Backend.Controllers;

import Guthub.Backend.Dtos.LoginDto;
import Guthub.Backend.Dtos.TokenDto;
import Guthub.Backend.Services.Authentication.AuthenticationService;
import Guthub.Backend.Services.Token.TokenPair;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController
{
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/login")
    public TokenDto login(HttpServletResponse response,
                          @RequestBody LoginDto loginDto)
    {
        TokenPair tokens = authenticationService.login(
                loginDto.getUsername(),
                loginDto.getPassword()
        );

        Cookie refreshCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);

        return new TokenDto(tokens.getAccessToken());
    }
}