package io.todak.project.myauthservice.jwt;

import io.todak.project.myauthservice.repository.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final static String BEARER = "Bearer ";
    private final static String REFRESH_TOKEN = "Refresh-Token";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        resolveAuthorizationToken(request).ifPresent(
                (authorizationToken) -> {
                    log.info("token exist : {}", authorizationToken);

                    if (!tokenProvider.isTokenExpired(authorizationToken)) {
                        // 토큰이 만료되지 않은 경우.
                        memoInSecurityContextHolder(authorizationToken);
                    } else {
                        //TODO: 토큰이 있지만, expired 된 경우
                        resolveRefreshToken(request).ifPresent((refreshToken) -> {
                            refreshTokenRepository.findUserIdByToken(refreshToken)
                                    .filter(userId -> userId.equals(tokenProvider.extractUserId(refreshToken)))
                                    .ifPresent(userId -> {

                                    });

                        });
                    }
                });
        filterChain.doFilter(request, response);
    }

    private void memoInSecurityContextHolder(String token) {
        SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(token));
    }

    private Optional<String> resolveAuthorizationToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return resolveJWT(authHeader);
    }

    private Optional<String> resolveRefreshToken(HttpServletRequest request) {
        String refreshTokenHeader = request.getHeader(REFRESH_TOKEN);
        return resolveJWT(refreshTokenHeader);
    }

    private Optional<String> resolveJWT(String token) {
        String jwt = null;
        if (StringUtils.hasText(token) && token.startsWith(BEARER)) {
            jwt = token.substring(BEARER.length());
        }
        return Optional.ofNullable(jwt);
    }


}
