package com.tta.geumgiri.loan.application.scheduler;

import com.tta.geumgiri.loan.application.LoanService;
import com.tta.geumgiri.loan.domain.Loan;
import com.tta.geumgiri.loan.persistence.LoanRepository;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


// 스케줄러를 사용해서 자동이체 설정
@Component
public class LoanRepaymentScheduler {

    private final LoanRepository loanRepository;
    private final LoanService loanService;

    @Autowired
    public LoanRepaymentScheduler(LoanRepository loanRepository, LoanService loanService) {
        this.loanRepository = loanRepository;
        this.loanService = loanService;
    }

    @Scheduled(cron = "0 0/1 * * * ?") // 1분마다 실행
    @Transactional
    public void processLoanRepayment() {

        // 메시지를 저장할 StringBuilder
        StringBuilder messageBuilder = new StringBuilder();

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
                        messageBuilder.append("사용자 ").append(loan.getMember().getName()).append("이(가) 대출한 대출(").append(loan.getId()).append(")의 상환이 완료되었습니다: ").append(loan.getPaidInstallments()).append("/").append(loan.getInstallments()).append("회 상환 완료되었습니다.\n");
                    } else {
                        messageBuilder.append("사용자 ").append(loan.getMember().getName()).append("이(가) 대출한 대출(").append(loan.getId()).append(")의 일부가 상환되었습니다: ").append(loan.getPaidInstallments()).append("/").append(loan.getInstallments()).append("회차가 상환되었습니다.\n");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("잔액 부족으로 대출 상환 실패: " + e.getMessage());
                }
            }

        }
        // 턴이 끝날 때 구분선과 함께 출력
        System.out.println(messageBuilder.toString());
        System.out.println("-------------------------------------------------");

        // 메시지 초기화
        messageBuilder.setLength(0);

    }
}
