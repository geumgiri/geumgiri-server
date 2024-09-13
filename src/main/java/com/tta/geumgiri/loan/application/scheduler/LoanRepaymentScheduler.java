package com.tta.geumgiri.loan.application.scheduler;

import com.tta.geumgiri.loan.domain.Loan;
import com.tta.geumgiri.loan.persistence.LoanRepository;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LoanRepaymentScheduler {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanRepaymentScheduler(LoanRepository loanRepository, AccountRepository accountRepository) {
        this.loanRepository = loanRepository;
    }

    @Scheduled(cron = "0 0 0/1 * * ?") // 1시간마다 이벤트 발생
    public void processLoanRepayment() {
        LocalDate today = LocalDate.now();
        List<Loan> loans = loanRepository.findByRepaymentDate(today);

        for (Loan loan : loans) {
            if (!loan.isPaid()) {
                // 대출 상환 처리
                Account account = loan.getAccount();

                // 대출 금액만큼 차감
                try {
                    account.deductBalance(loan.getAmount());
                    loan.markAsPaid(); // 대출 상환 완료 처리
                    loanRepository.save(loan);
                } catch (IllegalArgumentException e) {
                    System.out.println("잔액 부족으로 대출 상환 실패: " + e.getMessage());
                }
            }
        }
    }
}
