package com.tta.geumgiri.card.service;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import com.tta.geumgiri.card.domain.Card;
import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.card.domain.dto.*;
import com.tta.geumgiri.card.repository.CardRepository;
import com.tta.geumgiri.card.repository.MyCardRepository;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.ACCOUNT_NOT_FOUND)));

        if (isDuplicateAccount(account)) {
            throw new IllegalArgumentException(String.valueOf(ErrorStatus.DUPLICATE_ACCOUNT_ID));
        }

        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.CARD_NOT_FOUND)));

        MyCard myCard = MyCard.builder()
                .card(card)
                .account(account)
                .cardPoint(0)
                .build();

        myCardRepository.save(myCard);

        return new MyCardResponse(myCard);
    }

    @Transactional(readOnly = true)
    public List<MyCardListResponse> getMyCards(Long memberId) {

        List<MyCard> myCards = myCardRepository.findByAccountMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.CARD_NOT_FOUND)));

        return myCards
                .stream()
                .map(MyCardListResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public MyCardResponse getMyCard(Long myCardId) {
        MyCard myCard = myCardRepository.findById(myCardId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.CARD_NOT_FOUND)));

        return new MyCardResponse(myCard);
    }

    @Transactional
    public void removeMyCard(Long myCardId) {
        MyCard myCard = myCardRepository.findById(myCardId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.CARD_NOT_FOUND)));

        myCardRepository.delete(myCard);
    }

    private boolean isDuplicateAccount(Account account) {
        Optional<MyCard> myCard = myCardRepository.findByAccount(account);
        return myCard.isPresent();
    }
}
