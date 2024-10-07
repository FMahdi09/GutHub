package guthub.backend.services.authentication.exceptions;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

@StandardException
public class InvalidRefreshTokenException extends AuthenticationException
{
}
