package com.tta.geumgiri.account.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

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
    }
}
