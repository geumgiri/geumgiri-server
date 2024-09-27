package com.tta.geumgiri.savings.application;

import com.tta.geumgiri.savings.domain.Deposit;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.savings.persistence.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepositService {

    private final DepositRepository depositRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    public Deposit createDeposit(Account account, Long amount, Double interestRate, int months) {
        LocalDateTime depositDate = LocalDateTime.now();
        LocalDateTime maturityDate = depositDate.plusMonths(months);
        Deposit deposit = new Deposit(account, amount, depositDate, maturityDate, interestRate);
        return depositRepository.save(deposit);
    }

    public void processMaturedDeposit(Deposit deposit) {
        if (deposit.getMaturityDate().isBefore(LocalDateTime.now())) {
            Long maturedAmount = deposit.calculateMaturedAmount();
            deposit.getAccount().addBalance(maturedAmount);
            // 여기서 적절히 deposit을 만료 처리
        }
    }

    public List<Deposit> getDepositsByAccountId(Account account) {
        return depositRepository.findAllByAccount(account);
    }
}
