package io.todak.project.myauthservice.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@ConfigurationProperties("jwt")
public class JwtConfigurationProperty {

    private static final long HOUR = 1000L * 60L * 60L;

    @Getter
    private String secret;

    private long tokenValidityInHour;
    private long refreshTokenValidityInHour;

    public long getTokenValidityTime() {
        return this.tokenValidityInHour * HOUR;
    }

    public long getRefreshTokenValidityTime() {
        return this.refreshTokenValidityInHour * HOUR;
    }

}
