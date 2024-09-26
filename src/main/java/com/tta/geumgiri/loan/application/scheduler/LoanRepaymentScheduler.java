package com.tta.geumgiri.loan.application.scheduler;

import com.tta.geumgiri.loan.application.LoanService;
import com.tta.geumgiri.loan.domain.Loan;
import com.tta.geumgiri.loan.persistence.LoanRepository;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


// 스케줄러를 사용해서 자동으로
@Component
public class LoanRepaymentScheduler {

    private final LoanRepository loanRepository;
    private final LoanService loanService;

    @Autowired
    public LoanRepaymentScheduler(LoanRepository loanRepository, LoanService loanService) {
        this.loanRepository = loanRepository;
        this.loanService = loanService;
    }

    @Scheduled(cron = "0 0/1 * * * ?") // 3분마다 실행
    public void processLoanRepayment() {
        LocalDate today = LocalDate.now();
        List<Loan> loans = loanRepository.findByRepaymentDate(today);

        for (Loan loan : loans) {
            if (!loan.isPaid()) {
                if (loan.getInstallments() <= 0){
                    System.out.println("상환 횟수가 0인 대출은 처리할 수 없습니다.");
                    continue;
                }
                try {
                    loanService.payInstallment(loan);

                    if (loan.isPaid()) {
                        System.out.println("대출 상환 완료: " + loan.getPaidInstallments() + "/" + loan.getInstallments() + "회 상환 완료되었습니다.");
                    } else {
                        System.out.println("대출 일부 상환: " + loan.getPaidInstallments() + "/" + loan.getInstallments() + "회차가 상환되었습니다.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("잔액 부족으로 대출 상환 실패: " + e.getMessage());
                }
            }
        }
    }
}
