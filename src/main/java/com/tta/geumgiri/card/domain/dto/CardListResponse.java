package com.tta.geumgiri.card.domain.dto;

import com.tta.geumgiri.card.domain.Card;
import lombok.Getter;

@Getter
public class CardListResponse {

        private final Long id;
        private final String cardName;

        public CardListResponse(Card card) {
                this.id = card.getId();
                this.cardName = card.getCardName();
        }
}