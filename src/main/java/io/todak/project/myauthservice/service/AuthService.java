package io.todak.project.myauthservice.service;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.exception.InvalidRefreshTokenException;
import io.todak.project.myauthservice.exception.UsernameNotFoundException;
import io.todak.project.myauthservice.jwt.TokenProvider;
import io.todak.project.myauthservice.repository.AccountRepository;
import io.todak.project.myauthservice.repository.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public Token generateNewTokenIfValidRefreshToken(String token) {

        Long userId = refreshTokenRepository.findUserIdByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException(token));


        Account account = accountRepository.findById(userId)
                .orElseThrow(UsernameNotFoundException::new);


        String accessToken = tokenProvider.generate(account.getId(), account.getUsername());
        String refreshToken = tokenProvider.generateRefresh(account.getId(), account.getUsername());

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
