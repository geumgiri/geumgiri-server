package com.tta.geumgiri.account.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String accountName;
    private String accountNumber;

    @Setter
    private Long balance; // 계좌 잔액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonBackReference // 직렬화 제외
    private Member member;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MyCard> myCards = new ArrayList<>();

    @Builder
    public Account(String accountName, String accountNumber, Member member) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.member = member;
        this.balance = 0L; // 초기 잔액 0으로 설정
    }

    // 잔액을 증가/차감하는 메서드
    public void deductBalance(Long amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
    }
}
