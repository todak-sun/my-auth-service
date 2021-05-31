package io.todak.project.myauthservice.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@ConfigurationProperties("jwt")
public class JwtConfigurationProperty {

    @Getter
    private String secret;

    private long tokenValidityInMilliseconds;
    private long refreshTokenValidityInMilliseconds;

    public long getTokenValidityTime() {
        return this.tokenValidityInMilliseconds * 1000L;
    }

    public long getRefreshTokenValidityTime() {
        return this.refreshTokenValidityInMilliseconds * 1000L;
    }

}
