package com.tta.geumgiri.loan.application;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import com.tta.geumgiri.loan.domain.Loan;
import com.tta.geumgiri.loan.persistence.LoanRepository;
import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.persistence.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDate;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, MemberRepository memberRepository, AccountRepository accountRepository) {
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
    }

    public Loan applyForLoan(Long memberId, Long amount, String accountNumber, LocalDate repaymentDate, int installments) {

        if (installments <= 0) {
            throw new IllegalArgumentException("상환 횟수는 1 이상이어야 합니다.");
        }

        // 회원 정보 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (repaymentDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("상환 날짜는 오늘보다 과거일 수 없습니다.");
        }

        // 신용등급 검증 예: 신용등급 500 이상만 대출 가능
        if (member.getCreditRatio() < 500) {
            throw new IllegalArgumentException("신용등급이 낮아 대출이 불가능합니다.");
        }

        // 계좌 정보 확인
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // 계좌 소유자 확인
        if (!account.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("이 계좌는 해당 회원의 것이 아닙니다.");
        }

        account.addBalance(amount);
        // 대출 신청
        Loan loan = Loan.builder()
                .amount(amount)
                .repaymentDate(repaymentDate)
                .member(member)
                .account(account)
                .installments(installments)
                .build();

        return loanRepository.save(loan);
    }

    // loanId로 대출을 조회하고 상환 처리
    public void payInstallment(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        // 조회한 Loan 객체로 상환 처리
        payInstallment(loan);
    }

    // 실제 상환 로직 처리
    @Transactional
    public void payInstallment(Loan loan) {

        if (loan.getInstallments() <= 0) {
            throw new IllegalStateException("상환 횟수가 0입니다. 올바른 값을 입력해야 합니다.");
        }

        Long installmentAmount = loan.getAmount() / loan.getInstallments();

        if (loan.getRemainingAmount() < installmentAmount) {
            throw new IllegalArgumentException("상환 금액이 남은 금액보다 큽니다.");
        }

        Account account = loan.getAccount();
        account.deductBalance(installmentAmount);
        loan.setRemainingAmount(loan.getRemainingAmount() - installmentAmount);
        loan.incrementPaidInstallments(); // 상환된 회차 증가

        if (loan.getRemainingAmount() == 0) {
            loan.markAsPaid(); // 상환 완료
        }

        loanRepository.save(loan);
    }

    public Loan getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
    }
}
