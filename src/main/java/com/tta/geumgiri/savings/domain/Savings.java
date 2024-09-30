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
    private int minutesElapsed;  // 1분 단위로 수정

    // 적금 계좌 생성 시 초기 금액 필드 추가
    private Long initialAmount;

    public Savings(Account account, Long monthlyDepositAmount, Long initialAmount, LocalDateTime startDate, LocalDateTime endDate, Double interestRate) {
        this.account = account;
        this.monthlyDepositAmount = monthlyDepositAmount;
        this.initialAmount = initialAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.interestRate = interestRate;
        this.minutesElapsed = 0;  // 초기화
    }

    // 적금 이자 계산
    public Long calculateTotalSavings() {
        Long totalPrincipal = monthlyDepositAmount * minutesElapsed;
        return (long) (totalPrincipal + (totalPrincipal * interestRate / 100));
    }

    // 적금에 1분마다 금액 추가
    public void addMinuteDeposit() {
        minutesElapsed++;
    }
}
