package io.todak.project.myauthservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.todak.project.myauthservice.security.SecurityAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableConfigurationProperties(JwtConfigurationProperty.class)
public class TokenProvider implements InitializingBean {

    private final JwtConfigurationProperty property;

    private final static String ACCESS_TOKEN_NAME = "accessToken";
    private final static String REFERESH_TOKEN_NAME = "refreshToken";


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

    public boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);
        long userId = Long.parseLong(claims.getSubject());
        String username = claims.get("username", String.class);
        return new UsernamePasswordAuthenticationToken(new SecurityAccount(userId, username), token);
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token)
                .getSubject());
    }


    private Claims extractAllClaims(String token) throws ExpiredJwtException {
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
