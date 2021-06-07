package io.todak.project.myauthservice.service;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.exception.InvalidRefreshTokenException;
import io.todak.project.myauthservice.exception.UsernameNotFoundException;
import io.todak.project.myauthservice.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenServiceTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SignService signService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RedisTemplate<String, Long> redisTemplate;

    @BeforeEach
    public void beforeEach() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }


    @DisplayName("빈으로 등록")
    @Test
    public void is_bean() {
        assertNotNull(refreshTokenService);
    }

    @DisplayName("성공 테스트")
    @Test
    public void generate_new_token_success() {
        ValueOperations<String, Long> valueOps = redisTemplate.opsForValue();

        // given
        String username = "tjsdydwn@gmail.com";
        String password = "asdf@1234";
        Account account = signService.signUp(username, password);
        Token oldToken = signService.signIn(username, password);

        //when
        Token newToken = refreshTokenService.generateNewTokenIfValidRefreshToken(oldToken.getRefreshToken());

        //then
        assertNotNull(valueOps.get(newToken.getRefreshToken()), "새롭게 발행된 refresh token이 저장되어 있다.");
        assertEquals(valueOps.get(newToken.getRefreshToken()), account.getId(), "새롭게 발행된 refresh token을 key로, value는 userId 로 한다.");
    }

    @DisplayName("존재하지 않는 refresh token")
    @Test
    public void try_generate_new_token_with_not_exsist_refresh_token() {
        assertThrows(InvalidRefreshTokenException.class,
                () -> refreshTokenService.generateNewTokenIfValidRefreshToken("NOT_EXSIST_TOKEN"),
                "존재하지 않는 refresh token으로 요청시, 에러가 난다.");
    }

    @DisplayName("Refersh Token이 발행된 사용자가, 탈퇴했을 경우")
    @Test
    public void try_generate_new_token_when_user_exit() {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "asdf@1234";
        Account account = signService.signUp(username, password);

        Token token = signService.signIn(username, password);
        accountRepository.delete(account);

        //when & then
        assertThrows(UsernameNotFoundException.class,
                () -> {
                    refreshTokenService.generateNewTokenIfValidRefreshToken(token.getRefreshToken());
                },
                "탈퇴한 사용자의 아이디로 refresh token 요청이 오면 에러를 낸다.");
    }

}