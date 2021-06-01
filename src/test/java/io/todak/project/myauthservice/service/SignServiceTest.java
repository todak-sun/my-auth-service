package io.todak.project.myauthservice.service;

import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.exception.DuplicateException;
import io.todak.project.myauthservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SignServiceTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SignService signService;

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("빈으로 등록되어 있다.")
    @Test
    public void is_bean() {
        assertNotNull(signService, "signService는 null이 아니다.");

        SignService beanSignService = context.getBean(SignService.class);
        assertEquals(signService, beanSignService, "스프링 Bean으로 등록되어 있다.");
    }

    @DisplayName("회원가입 서비스 성공 테스트")
    @Test
    public void sign_up_test_success() {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "password";

        //when
        Account newAccount = signService.signUp(username, password);

        //then
        assertNotNull(newAccount.getId(), () -> "새로 부여된 id가 있다.");
        assertNotNull(newAccount.getPassword(), () -> "패스워드는 null이 아니다.");
        assertNotNull(newAccount.getUsername(), () -> "username은 null이 아니다.");
        assertNotNull(newAccount.getCreatedDateTime(), () -> "회원가입한 시간이 기록된다.");
        assertNotNull(newAccount.getUpdatedDateTime(), () -> "회원가입한 시간이 updateDateTime에도 함께 기록된다.");

        assertNotEquals(newAccount.getPassword(), password, () -> "회원가입시 입력한 password와, DB에 저장된 password는 다르다.");

        log.info("id : {}", newAccount.getId());
    }

    @DisplayName("중복된 아이디로 회원가입을 시도하면 적절한 에러를 뱉어내는 테스트")
    @Test
    public void sign_up_test_duplicated_username() {
        // given
        String username = "tjsdydwn@gmail.com";
        String password = "password";

        // when
        Account first = signService.signUp(username, password);

        // then
        DuplicateException duplicateException = assertThrows(DuplicateException.class, () -> {
            Account second = signService.signUp(username, password);
        });

        Object resource = duplicateException.getResource();

        assertEquals(resource, username);
    }

}