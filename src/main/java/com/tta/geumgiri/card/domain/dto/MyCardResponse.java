package com.tta.geumgiri.card.domain.dto;

import com.tta.geumgiri.card.domain.MyCard;
import lombok.Getter;

@Getter
public class MyCardResponse {

    private final Long id;
    private final Long cardId;
    private final Long accountId;
    private final Integer cardPoint;

    public MyCardResponse(MyCard myCard) {
        this.id = myCard.getId();
        this.cardId = myCard.getCard().getId();
        this.accountId = myCard.getAccount().getId();
        this.cardPoint = myCard.getCardPoint();
    }
}
