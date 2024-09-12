package com.tta.geumgiri.card.service;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import com.tta.geumgiri.card.domain.Card;
import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.card.domain.dto.CardListResponse;
import com.tta.geumgiri.card.domain.dto.CardResponse;
import com.tta.geumgiri.card.domain.dto.MyCardRequest;
import com.tta.geumgiri.card.domain.dto.MyCardResponse;
import com.tta.geumgiri.card.repository.CardRepository;
import com.tta.geumgiri.card.repository.MyCardRepository;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final MyCardRepository myCardRepository;
    private final AccountRepository accountRepository;

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

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.CARD_NOT_FOUND)));

        return new CardResponse(card);
    }

    @Transactional
    public MyCardResponse createMyCard(MyCardRequest request) {

        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.CARD_NOT_FOUND)));

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.ACCOUNT_NOT_FOUND)));

        MyCard myCard = MyCard.builder()
                .card(card)
                .account(account)
                .cardPoint(0)
                .build();

        myCardRepository.save(myCard);

        return new MyCardResponse(myCard);
    }

}
