package io.todak.project.myauthservice.service;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.exception.DuplicateResourceException;
import io.todak.project.myauthservice.exception.InvalidPasswordException;
import io.todak.project.myauthservice.exception.NotFoundResourceException;
import io.todak.project.myauthservice.jwt.TokenProvider;
import io.todak.project.myauthservice.repository.AccountRepository;
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

    private final TokenProvider tokenProvider;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Token signIn(String username, String password) {
        Account existAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundResourceException(username));

        if (!passwordEncoder.matches(password, existAccount.getPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = tokenProvider.generate(existAccount.getId(), existAccount.getUsername());
        String refreshToken = tokenProvider.generateRefresh(existAccount.getId(), existAccount.getUsername());

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public Account signUp(String username, String password) {

        if (accountRepository.existsByUsername(username)) {
            throw new DuplicateResourceException(username);
        }

        Account account = Account.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        return accountRepository.save(account);
    }

}
