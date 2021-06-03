package io.todak.project.myauthservice.repository;

import io.todak.project.myauthservice.entity.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DisplayName("AccountRepository 테스트")
@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EntityManager em;

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("빈으로 등록되어 있다.")
    @Test
    public void is_bean() {
        AccountRepository repositoryBean = context.getBean(AccountRepository.class);
        assertEquals(repositoryBean, accountRepository);
        assertNotNull(accountRepository);
    }

    @DisplayName("Account를 저장하면, createDateTime이 함께 저장된다.")
    @Test
    public void save_with_create_date_time() {
        // given
        String username = "todaksun@gmail.com";
        String password = "password";
        Account account = Account.create(username, password, passwordEncoder);


        // when
        Account newAccount = accountRepository.save(account);

        // then
        assertNotNull(newAccount.getCreatedDateTime());
    }

    @DisplayName("Account를 저장하면, updateDateTime도 함께 저장된다.")
    @Test
    public void save_with_update_date_time() {
        // given
        String username = "todaksun@gmail.com";
        String password = "password";
        Account account = Account.create(username, password, passwordEncoder);

        // when
        Account newAccount = accountRepository.save(account);

        // then
        assertNotNull(newAccount.getLastModifiedDateTime());
    }

    @DisplayName("Entity를 수정하면, updateDateTime도 바뀐다.")
    @Test
    public void updated_update_date_time_after_change_some() {
        // given
        String username = "todaksun@gmail.com";
        String password = "password";

        Account account = Account.create(username, password, passwordEncoder);

        Account newAccount = accountRepository.save(account);
        Long accountId = newAccount.getId();
        LocalDateTime before = newAccount.getLastModifiedDateTime();

        em.flush();
        em.clear();

        Account foundedAccount = accountRepository.findById(accountId).get();
        foundedAccount.changePassword("newPassword", passwordEncoder);
        em.flush();
        em.clear();
        LocalDateTime after = foundedAccount.getLastModifiedDateTime();

        assertNotEquals(before, after);
    }

}