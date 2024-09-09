package com.tta.geumgiri.card.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyCardIssuanceRequest {

    private final Long accountId;
    private final Long cardId;
}