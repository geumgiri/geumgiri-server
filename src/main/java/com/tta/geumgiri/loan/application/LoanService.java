package com.tta.geumgiri.loan.application;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import com.tta.geumgiri.loan.domain.Loan;
import com.tta.geumgiri.loan.persistence.LoanRepository;
import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.persistence.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Loan applyForLoan(Long memberId, Long amount, String accountNumber, LocalDate repaymentDate) {
        // 회원 정보 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 신용등급 검증 예: 신용등급 500 이상만 대출 가능
        if (member.getCreditRatio() < 400) {
            throw new IllegalArgumentException("신용등급이 낮아 대출이 불가능합니다.");
        }

        // 계좌 정보 확인
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // 대출 신청
        Loan loan = Loan.builder()
                .amount(amount)
                .repaymentDate(repaymentDate)
                .member(member)
                .account(account)
                .build();

        return loanRepository.save(loan);
    }
}
