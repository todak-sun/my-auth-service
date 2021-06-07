package io.todak.project.myauthservice.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.todak.project.myauthservice.security.SecurityAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableConfigurationProperties(JwtConfigurationProperty.class)
public class TokenProvider implements InitializingBean {
    public final static String REFERESH_TOKEN_HEADER = "Refresh-Token";

    private final JwtConfigurationProperty property;
    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(property.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(Long userId, String username) {
        return createToken(userId, username, property.getTokenValidityTime());
    }

    public String generateRefresh(Long userid, String username) {
        return createToken(userid, username, property.getRefreshTokenValidityTime());
    }

    public boolean assertValid(String token) {
        Claims claims = null;
        try {
            claims = extractAllClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            e.printStackTrace();
            log.error("잘못된 형식의 토큰");
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            log.error("만료된 토큰");
        } catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
            log.error("지원되지 않는 Jwt 형식");
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            log.error("잘못된 토큰");
        }

        return false;
    }

    public SecurityAccount extractSecurityAccount(String token) {
        Claims claims = extractAllClaims(token);
        long userId = Long.parseLong(claims.getSubject());
        String username = claims.get("username", String.class);
        return new SecurityAccount(userId, username);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String createToken(Long userId, String username, long expiration) {
        Assert.notNull(userId, "userId is required!");

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
