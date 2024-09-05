package com.tta.geumgiri.card.domain;

import com.tta.geumgiri.account.domain.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "my_cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MyCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "card_point")
    private Integer cardPoint;

    @Builder
    public MyCard(Card card, Account account, int cardPoint) {
        this.card = card;
        this.account = account;
        this.cardPoint = cardPoint;
    }
}
