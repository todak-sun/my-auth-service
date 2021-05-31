package io.todak.project.myauthservice.repository;

import io.todak.project.myauthservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
