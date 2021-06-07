package io.todak.project.myauthservice.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final static String BEARER = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        resolveAuthorizationToken(request).ifPresent(
                (authorizationToken) -> {
                    log.info("token exist : {}", authorizationToken);
                    log.info("check validation : {} ", tokenProvider.assertValid(authorizationToken));
                    if (tokenProvider.assertValid(authorizationToken)) {
                        log.info("token not expired. put SecurityContextHolder");
                        memoInSecurityContextHolder(authorizationToken, request);
                    }
                });
        filterChain.doFilter(request, response);
    }

    private void memoInSecurityContextHolder(String token, HttpServletRequest request) {
        var account = tokenProvider.extractSecurityAccount(token);
        var authenticationToken = new UsernamePasswordAuthenticationToken(account, token, Collections.emptyList());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        log.info("usernamePasswordAuthenticationToken : {}", authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private Optional<String> resolveAuthorizationToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return resolveJWT(authHeader);
    }

    private Optional<String> resolveJWT(String token) {
        String jwt = null;
        if (StringUtils.hasText(token) && token.startsWith(BEARER)) {
            jwt = token.substring(BEARER.length());
        }
        return Optional.ofNullable(jwt);
    }


}
