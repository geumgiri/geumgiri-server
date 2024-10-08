package com.tta.geumgiri.card.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "benefits")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_type", nullable = false)
    private BenefitType benefitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_category", nullable = false)
    private BenefitCategory benefitCategory;

    @Column(name = "value", nullable = false)
    private Double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Benefit(BenefitType benefitType, BenefitCategory benefitCategory, Double value, Card card) {
        this.benefitType = benefitType;
        this.benefitCategory = benefitCategory;
        this.value = value;
        this.card = card;
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
