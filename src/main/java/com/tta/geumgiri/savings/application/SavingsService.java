package com.tta.geumgiri.savings.application;

import com.tta.geumgiri.savings.domain.Savings;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.savings.persistence.SavingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SavingsService {

    private final SavingsRepository savingsRepository;

    @Autowired
    public SavingsService(SavingsRepository savingsRepository) {
        this.savingsRepository = savingsRepository;
    }

    public Savings createSavings(Account account, Long monthlyDepositAmount, Double interestRate, int months) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(months);
        Savings savings = new Savings(account, monthlyDepositAmount, startDate, endDate, interestRate);
        return savingsRepository.save(savings);
    }

    public void processMonthlySavings(Savings savings) {
        if (savings.getEndDate().isAfter(LocalDateTime.now())) {
            savings.addMonthlyDeposit();
            savings.getAccount().deductBalance(savings.getMonthlyDepositAmount());
        }
    }

    public List<Savings> getAllSavings() {
        return savingsRepository.findAll();
    }

    public List<Savings> getSavingsByAccountId(Account account) {
        return savingsRepository.findAllByAccount(account);
    }
}
