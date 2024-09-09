package com.tta.geumgiri.card.domain.dto;

import com.tta.geumgiri.card.domain.Benefit;
import com.tta.geumgiri.card.domain.Card;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CardResponse {

    private final Long id;
    private final String cardName;
    private final String cardDescription;
    private final List<Long> benefitIds;

    public CardResponse(Card card) {
        this.id = card.getId();
        this.cardName = card.getCardName();
        this.cardDescription = card.getCardDescription();
        this.benefitIds = card.getBenefits().stream()
                .map(Benefit::getId)
                .collect(Collectors.toList());
    }
}