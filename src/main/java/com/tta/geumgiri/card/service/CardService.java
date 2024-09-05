package com.tta.geumgiri.card.service;

import com.tta.geumgiri.card.domain.Card;
import com.tta.geumgiri.card.domain.dto.CardListResponse;
import com.tta.geumgiri.card.domain.dto.CardResponse;
import com.tta.geumgiri.card.repository.BenefitRepository;
import com.tta.geumgiri.card.repository.CardRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final BenefitRepository benefitRepository;

    @Transactional(readOnly = true)
    public List<CardListResponse> getCards() {

        List<Card> cards = cardRepository.findAll();

        return cards
                .stream()
                .map(CardListResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public CardResponse getCard(Long cardId) {

        Card card = cardRepository.findById(cardId).orElse(null); // 예외처리 추가하기
        // .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));

        return new CardResponse(card);
    }
}
