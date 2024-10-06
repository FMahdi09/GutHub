package guthub.backend.security;

import guthub.backend.services.token.exceptions.ExpiredTokenException;
import guthub.backend.services.token.exceptions.InvalidTokenException;
import guthub.backend.services.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        if (request.getRequestURI().contains("/auth/"))
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
