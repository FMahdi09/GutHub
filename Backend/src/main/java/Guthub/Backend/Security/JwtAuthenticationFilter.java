package Guthub.Backend.Security;

import Guthub.Backend.Services.Token.Exceptions.ExpiredTokenException;
import Guthub.Backend.Services.Token.Exceptions.InvalidTokenException;
import Guthub.Backend.Services.Token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException
    {
        if (request.getServletPath().contains("api/v1/auth"))
        {
            filterChain.doFilter(request, response);
            return;
        }

        try
        {
            final String accessToken = getTokenFromRequest(request);

            final String username = tokenService.getSubjectFromAccessToken(accessToken);

            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        }
        catch (InvalidTokenException | ExpiredTokenException ex)
        {
            filterChain.doFilter(request, response);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request)
            throws InvalidTokenException
    {
        String bearerToken = request.getHeader("Authorization");

        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer "))
        {
            throw new InvalidTokenException("malformed authorization header");
        }

        return bearerToken.substring(7);
    }
}
