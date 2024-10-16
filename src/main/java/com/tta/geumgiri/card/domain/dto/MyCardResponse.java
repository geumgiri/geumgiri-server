package com.tta.geumgiri.card.domain.dto;

import com.tta.geumgiri.card.domain.MyCard;
import lombok.Getter;

@Getter
public class MyCardResponse {

    private final Long id;
    private final Long cardId;
    private final String cardName;
    private final String account;
    private final Integer cardPoint;

    public MyCardResponse(MyCard myCard) {
        this.id = myCard.getId();
        this.cardId = myCard.getCard().getId();
        this.cardName = myCard.getCard().getCardName();
        this.account = myCard.getAccount().getAccountNumber();
        this.cardPoint = myCard.getCardPoint();
    }
}
