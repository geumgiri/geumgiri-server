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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private Long monthlyDepositAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double interestRate;
    private int monthsElapsed;

    // 적금 계좌 생성 시 초기 금액 필드 추가
    private Long initialAmount;

    public Savings(Account account, Long monthlyDepositAmount, Long initialAmount, LocalDateTime startDate, LocalDateTime endDate, Double interestRate) {
        this.account = account;
        this.monthlyDepositAmount = monthlyDepositAmount;
        this.initialAmount = initialAmount; // 초기 금액 저장
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
