package com.tta.geumgiri.savings.domain;

import com.tta.geumgiri.account.domain.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private Long amount;
    private LocalDateTime depositDate;
    private LocalDateTime maturityDate;
    private Double interestRate;

    public Deposit(Account account, Long amount, LocalDateTime depositDate, LocalDateTime maturityDate, Double interestRate) {
        this.account = account;
        this.amount = amount;
        this.depositDate = depositDate;
        this.maturityDate = maturityDate;
        this.interestRate = interestRate;
    }

    // 만기 이자 계산
    public Long calculateMaturedAmount() {
        return (long) (amount + (amount * interestRate / 100));
    }
}