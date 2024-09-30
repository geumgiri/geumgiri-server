package com.tta.geumgiri.savings.application;

import com.tta.geumgiri.account.application.AccountService;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.common.exception.BusinessException;
import com.tta.geumgiri.common.exception.NotFoundException;
import com.tta.geumgiri.savings.domain.Deposit;
import com.tta.geumgiri.savings.domain.Savings;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.savings.persistence.DepositRepository;
import com.tta.geumgiri.savings.persistence.SavingsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SavingsService {

    private final SavingsRepository savingsRepository;
    private final DepositRepository depositRepository;
    private final AccountService accountService;

    @Autowired
    public SavingsService(SavingsRepository savingsRepository, DepositRepository depositRepository, AccountService accountService) {
        this.savingsRepository = savingsRepository;
        this.depositRepository = depositRepository;
        this.accountService = accountService;
    }

    public Object createSavingsOrDeposit(Long memberId, Long amount, Double interestRate, int months, String type, String accessToken, Long monthlyDepositAmount) {
        // 1. 계좌 조회
        Account account = accountService.getAccountsByMemberId(memberId, accessToken).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ACCOUNT_NOT_FOUND));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(months);

        // 2. 예금 또는 적금 구분
        if (type.equalsIgnoreCase("deposit")) {
            // 예금 생성 로직
            Deposit deposit = new Deposit(account, amount, startDate, endDate, interestRate);
            account.deductBalance(amount);  // 계좌에서 금액 차감
            return depositRepository.save(deposit);
        } else if (type.equalsIgnoreCase("savings")) {
            if (monthlyDepositAmount == null) {
                throw new BusinessException(ErrorStatus.MISSING_MONTHLY_DEPOSIT);
            }
            // 적금 생성 로직
            Savings savings = new Savings(account, monthlyDepositAmount, amount, startDate, endDate, interestRate);
            account.deductBalance(amount);  // 계좌에서 초기 금액 차감
            return savingsRepository.save(savings);
        } else {
            throw new BusinessException(ErrorStatus.INVALID_SAVINGS_TYPE);
        }
    }

    // 적금의 월 납입 처리
    @Transactional
    public void processMonthlySavings(Savings savings) {
        if (savings.getEndDate().isAfter(LocalDateTime.now())) {
            savings.addMinuteDeposit();
            if (savings.getAccount().getBalance() < savings.getMonthlyDepositAmount()) {
                throw new BusinessException(ErrorStatus.INSUFFICIENT_FUNDS);
            }
            savings.getAccount().deductBalance(savings.getMonthlyDepositAmount());
        }
    }

    // 예금 만료 처리
    public void processMaturedDeposit(Deposit deposit) {
        if (deposit.getMaturityDate().isBefore(LocalDateTime.now())) {
            Long maturedAmount = deposit.calculateMaturedAmount();
            deposit.getAccount().addBalance(maturedAmount);
            // 예금 만료 처리 로직 추가
        }
    }

    public List<Savings> getAllSavings() {
        return savingsRepository.findAll();
    }

    public List<Savings> getSavingsByAccountId(Account account) {
        return savingsRepository.findAllByAccount(account);
    }

    public List<Deposit> getDepositsByAccountId(Account account) {
        return depositRepository.findAllByAccount(account);
    }
}
