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
public class RefreshTokenService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public Token generateNewTokenIfValidRefreshToken(String refreshToken) {

        Long userId = refreshTokenRepository.findUserIdByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException(refreshToken));


        Account account = accountRepository.findById(userId)
                .orElseThrow(UsernameNotFoundException::new);

        String newAccessToken = tokenProvider.generate(account.getId(), account.getUsername());
        String newRefreshToken = tokenProvider.generateRefresh(account.getId(), account.getUsername());

        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.save(newRefreshToken, account.getId());

        return Token.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }


}
