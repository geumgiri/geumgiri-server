package com.tta.geumgiri.card.domain.dto;

import com.tta.geumgiri.card.domain.MyCard;
import lombok.Getter;

@Getter
public class MyCardListResponse {

    private final Long id;
    private final Long cardId;

    public MyCardListResponse(MyCard myCard) {
        this.id = myCard.getId();
        this.cardId = myCard.getCard().getId();
    }
}