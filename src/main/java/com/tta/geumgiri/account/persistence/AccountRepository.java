package com.tta.geumgiri.account.persistence;

import com.tta.geumgiri.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
