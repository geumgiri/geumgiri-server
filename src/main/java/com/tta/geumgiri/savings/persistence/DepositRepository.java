package com.tta.geumgiri.savings.persistence;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.savings.domain.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
    List<Deposit> findAllByAccount(Account account);
}
