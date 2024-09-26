package com.tta.geumgiri.loan.domain;

import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.account.domain.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private Long amount; // 대출 금액
    @Setter
    private Long remainingAmount; // 남은 상환 금액
    private LocalDate repaymentDate; // 상환 만기 날짜
    private int installments; // 전체 상환 횟수
    private int paidInstallments = 0; // 상환된 회차 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private boolean isPaid; // 대출 상환 여부

    @Builder
    public Loan(Long amount, LocalDate repaymentDate, Member member, Account account, int installments) {
        this.amount = amount;
        this.remainingAmount = amount; // 처음엔 전체 금액이 남아 있음
        this.repaymentDate = repaymentDate;
        this.member = member;
        this.account = account;
        this.isPaid = false; // 대출 신청 시 상환 여부는 false
        this.installments = installments;
    }


    public void incrementPaidInstallments() {
        this.paidInstallments++;
    }

    public void markAsPaid() {
        this.isPaid = true;
    }

}
