package io.todak.project.myauthservice.service;

import io.todak.project.myauthservice.jwt.TokenProvider;
import io.todak.project.myauthservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignService {

    private final TokenProvider tokenProvider;
    private final AccountRepository accountRepository;

    public void signUp(String username, String password) {

    }

    public void signIn(String username, String password) {
        accountRepository.findByUsername(username);
    }

}
