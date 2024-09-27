package com.tta.geumgiri.savings.application;

import com.tta.geumgiri.account.application.AccountService;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.common.exception.InsufficientFundsException;
import com.tta.geumgiri.common.exception.NotFoundException;
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
    private final AccountService accountService;

    @Autowired
    public SavingsService(SavingsRepository savingsRepository, AccountService accountService) {
        this.savingsRepository = savingsRepository;
        this.accountService = accountService;
    }

    public Savings createSavings(Long memberId, Long monthlyDepositAmount, Long initialAmount, Double interestRate, int months, String authHeader) {
        String accessToken = authHeader.replace("Bearer ", "");

        Account account = accountService.getAccountsByMemberId(memberId, accessToken).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ACCOUNT_NOT_FOUND));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(months);

        // 적금 계좌 생성
        Savings savings = new Savings(account, monthlyDepositAmount, initialAmount, startDate, endDate, interestRate);

        // 초기 금액 입금
        account.deductBalance(initialAmount); // 계좌에서 초기 금액 차감

        return savingsRepository.save(savings);
    }

    public void processMonthlySavings(Savings savings) {
        if (savings.getEndDate().isAfter(LocalDateTime.now())) {
            // 월 적금 추가
            savings.addMonthlyDeposit();

            // 계좌 잔액 확인
            if (savings.getAccount().getBalance() < savings.getMonthlyDepositAmount()) {
                throw new InsufficientFundsException(ErrorStatus.INSUFFICIENT_FUNDS);
            }
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
