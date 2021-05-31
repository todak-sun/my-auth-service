package io.todak.project.myauthservice.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;


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

    @DisplayName("wow")
    @Test
    public void create_token_test() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method createToken = TokenProvider.class.getDeclaredMethod("createToken", Long.class, String.class, long.class);
        createToken.setAccessible(true);
        Object result = createToken.invoke(tokenProvider, 1L, "todaksun@gmail.com", 1000L);
        log.info("token : {}", result);
    }

}