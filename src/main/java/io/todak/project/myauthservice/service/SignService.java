package io.todak.project.myauthservice.service;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.exception.DuplicateResourceException;
import io.todak.project.myauthservice.exception.InvalidPasswordException;
import io.todak.project.myauthservice.exception.UsernameNotFoundException;
import io.todak.project.myauthservice.jwt.TokenProvider;
import io.todak.project.myauthservice.repository.AccountRepository;
import io.todak.project.myauthservice.repository.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SignService {
    //TODO : SignInService와 SignUpService의 분리
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Token signIn(String username, String password) {
        Account existAccount = accountRepository.findByUsername(username)
                .orElseThrow(UsernameNotFoundException::new);

        if (!existAccount.hasSamePassword(password, passwordEncoder)) {
            throw new InvalidPasswordException();
        }

        String accessToken = tokenProvider.generate(existAccount.getId(), existAccount.getUsername());
        String refreshToken = tokenProvider.generateRefresh(existAccount.getId(), existAccount.getUsername());

        refreshTokenRepository.save(refreshToken, existAccount.getId());

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public Account signUp(String username, String password) {

        if (accountRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("username", username);
        }

        Account account = Account.create(username, password, passwordEncoder);

        return accountRepository.save(account);
    }

}
