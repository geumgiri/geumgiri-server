package com.tta.geumgiri.savings.domain;

import com.tta.geumgiri.account.domain.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Savings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private Long monthlyDepositAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double interestRate;
    private int monthsElapsed;

    public Savings(Account account, Long monthlyDepositAmount, LocalDateTime startDate, LocalDateTime endDate, Double interestRate) {
        this.account = account;
        this.monthlyDepositAmount = monthlyDepositAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.interestRate = interestRate;
        this.monthsElapsed = 0;
    }

    // 이자 계산
    public Long calculateTotalSavings() {
        Long totalPrincipal = monthlyDepositAmount * monthsElapsed;
        return (long) (totalPrincipal + (totalPrincipal * interestRate / 100));
    }

    // 적금에 금액 추가
    public void addMonthlyDeposit() {
        monthsElapsed++;
    }
}
