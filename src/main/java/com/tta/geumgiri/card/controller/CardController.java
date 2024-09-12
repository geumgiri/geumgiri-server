package com.tta.geumgiri.card.controller;

import com.tta.geumgiri.card.domain.dto.*;
import com.tta.geumgiri.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cards")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<List<CardListResponse>> getCards() {
        return ResponseEntity.ok(cardService.getCards());
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> getCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.getCard(cardId));
    }

    @PostMapping("/my-cards")
    public ResponseEntity<MyCardResponse> createMyCard(@RequestBody MyCardRequest request) {
        return ResponseEntity.ok(cardService.createMyCard(request));
    }

    @GetMapping("/my-cards")
    public ResponseEntity<List<MyCardListResponse>> getMyCards(@RequestParam Long memberId) {
        return ResponseEntity.ok(cardService.getMyCards(memberId));
    }

    @GetMapping("/my-cards/{myCardId}")
    public ResponseEntity<MyCardResponse> getMyCard(@PathVariable Long myCardId) {
        return ResponseEntity.ok(cardService.getMyCard(myCardId));
    }
}