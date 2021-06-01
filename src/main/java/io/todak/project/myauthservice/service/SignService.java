package io.todak.project.myauthservice.service;

import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.exception.DuplicateException;
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

    public void signIn(String username, String password) {
        accountRepository.findByUsername(username);
    }

    @Transactional
    public Account signUp(String username, String password) {

        if (accountRepository.existsByUsername(username)) {
            throw new DuplicateException(username);
        }

        Account account = Account.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

}
