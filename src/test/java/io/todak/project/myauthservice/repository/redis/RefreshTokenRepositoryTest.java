package io.todak.project.myauthservice.repository.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository repository;

    @DisplayName("빈으로 관리되고 있다.")
    @Test
    public void is_bean() {
        assertNotNull(repository);
    }

    @DisplayName("Key - Token, Value - UserId로 저장해보고, 꺼내보는 테스트")
    @Test
    public void saveTokenTest() {
        //given
        String refreshToken = "token";
        long userId = 1L;
        repository.save(refreshToken, userId);

        //when
        Optional<Long> userIdByToken = repository.findUserIdByToken(refreshToken);

        //then
        assertTrue(userIdByToken.isPresent());
        assertEquals(userId, userIdByToken.get());
    }

    @DisplayName("없는 키를 지우는 테스트")
    @Test
    public void delete_with_not_exsist_key(){
        boolean not_exsit = repository.delete("not_exsit");
        assertFalse(not_exsit);
    }


}