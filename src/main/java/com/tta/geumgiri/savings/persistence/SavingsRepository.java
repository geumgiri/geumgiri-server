package com.tta.geumgiri.savings.persistence;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.savings.domain.Savings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingsRepository extends JpaRepository<Savings, Long> {
    List<Savings> findAllByAccount(Account account);
}
