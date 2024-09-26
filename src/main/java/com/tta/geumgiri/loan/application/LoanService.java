package com.tta.geumgiri.loan.application;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
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
            throw new IllegalArgumentException(ErrorStatus.INVALID_INSTALLMENT.getMessage());
        }

        // 회원 정보 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        if (repaymentDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(ErrorStatus.INVALID_REPAYMENT_DATE.getMessage());
        }

        // 신용등급 500 이상만 대출 가능
        if (member.getCreditRatio() < 500) {
            throw new IllegalArgumentException(ErrorStatus.INSUFFICIENT_CREDIT.getMessage());
        }

        // 계좌 정보 확인
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException(ErrorStatus.ACCOUNT_NOT_FOUND.getMessage()));

        // 계좌 소유자 확인
        if (!account.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException(ErrorStatus.INVALID_ACCOUNT_OWNER.getMessage());
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
                .orElseThrow(() -> new IllegalArgumentException(ErrorStatus.LOAN_NOT_FOUND.getMessage()));

        // 조회한 Loan 객체로 상환 처리
        payInstallment(loan);
    }

    // 실제 상환 로직 처리
    @Transactional
    public void payInstallment(Loan loan) {

        if (loan.getInstallments() <= 0) {
            throw new IllegalStateException(ErrorStatus.INSTALLMENT_COUNT_ZERO.getMessage());
        }

        Long installmentAmount = loan.getAmount() / loan.getInstallments();

        if (loan.getRemainingAmount() < installmentAmount) {
            throw new IllegalArgumentException(ErrorStatus.INSTALLMENT_AMOUNT_EXCEEDS_REMAINING.getMessage());
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
                .orElseThrow(() -> new IllegalArgumentException(ErrorStatus.LOAN_NOT_FOUND.getMessage()));
    }
}
