package guthub.backend.services.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenPair
{
    private String accessToken;

    private String refreshToken;
}
