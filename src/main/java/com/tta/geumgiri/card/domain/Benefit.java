package com.tta.geumgiri.card.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
