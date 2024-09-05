package com.tta.geumgiri.card.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "card_description", nullable = false)
    private String cardDescription;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Benefit> benefits = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MyCard> myCards = new ArrayList<>();

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Card(String cardName, String cardDescription, List<Benefit> benefits, List<MyCard> myCards) {
        this.cardName = cardName;
        this.cardDescription = cardDescription;
        this.benefits = benefits;
        this.myCards = myCards;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
