package guthub.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "guthub")
@Getter
@Setter
public class MainConfiguration implements JwtConfiguration, FrontendConfiguration
{
    private String accessTokenSecret;

    private String refreshTokenSecret;

    private int accessTokenExpiration;

    private int refreshTokenExpiration;

    private String frontendUrl;
}
