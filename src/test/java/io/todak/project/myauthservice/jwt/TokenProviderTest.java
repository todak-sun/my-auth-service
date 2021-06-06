package io.todak.project.myauthservice.jwt;

import io.todak.project.myauthservice.security.SecurityAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
class TokenProviderTest {

    @Autowired
    TokenProvider tokenProvider;

    @DisplayName("빈으로 등록되어 있다.")
    @Test
    public void is_bean() {
        assertNotNull(tokenProvider);
    }

    @DisplayName("tokenProvider의 createToken을 테스트")
    @Test
    public void create_token_test() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // given
        Long userId = 1L;
        String username = "todaksun@gmail.com";

        Method createToken = TokenProvider.class.getDeclaredMethod("createToken", Long.class, String.class, long.class);
        createToken.setAccessible(true);

        // when
        String jwt = (String) createToken.invoke(tokenProvider, userId, username, 1000L);
        log.info("token : {}", jwt);

        SecurityAccount account = tokenProvider.extractSecurityAccount(jwt);

        //then
        assertAll(
                "토큰에 포함된 정보 확인",
                () -> assertEquals(userId, account.getUserId(), "userId가 있다."),
                () -> assertEquals(username, account.getUsername(), "username이 있다."),
                () -> assertFalse(tokenProvider.assertValid(jwt), "토큰 유효시간이 남아있다.")
        );
    }

}