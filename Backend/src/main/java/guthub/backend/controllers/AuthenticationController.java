package guthub.backend.controllers;

import guthub.backend.dtos.LoginDto;
import guthub.backend.dtos.TokenDto;
import guthub.backend.services.authentication.AuthenticationService;
import guthub.backend.services.token.TokenPair;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
                          @Valid @RequestBody LoginDto loginDto)
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
