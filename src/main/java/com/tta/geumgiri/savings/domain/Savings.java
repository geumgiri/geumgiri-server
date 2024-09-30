package com.tta.geumgiri.savings.domain;

import com.tta.geumgiri.account.domain.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int minutesElapsed = 0;

    // 적금 만료 처리
    // 만기 상태 필드
    @Setter
    private boolean matured = false;

    // 적금 계좌 생성 시 초기 금액 필드 추가
    private Long initialAmount;

    public Savings(Account account, Long monthlyDepositAmount, Long initialAmount, LocalDateTime startDate, LocalDateTime endDate, Double interestRate) {
        this.account = account;
        this.monthlyDepositAmount = monthlyDepositAmount;
        this.initialAmount = initialAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.interestRate = interestRate;
        this.minutesElapsed = 0;
        this.matured = false;
    }

    // 이자 계산
    public Long calculateTotalSavings() {
        Long totalPrincipal = monthlyDepositAmount * minutesElapsed;
        return (long) (totalPrincipal + (totalPrincipal * interestRate / 100));
    }

    // 적금에 1분마다 금액 추가
    public void addMinuteDeposit() {
        if (!matured) {
            minutesElapsed++;
        }
    }
}
