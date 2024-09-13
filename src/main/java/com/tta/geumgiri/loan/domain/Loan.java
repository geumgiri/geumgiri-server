package com.tta.geumgiri.loan.domain;

import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.account.domain.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

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
    private LocalDate repaymentDate; // 상환 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private boolean isPaid; // 대출 상환 여부

    @Builder
    public Loan(Long amount, LocalDate repaymentDate, Member member, Account account) {
        this.amount = amount;
        this.repaymentDate = repaymentDate;
        this.member = member;
        this.account = account;
        this.isPaid = false; // 대출 신청 시 상환 여부는 false
    }

    // 상환 처리 메서드
    public void markAsPaid() {
        this.isPaid = true;
    }
}
