package com.tta.geumgiri.card.repository;

import com.tta.geumgiri.card.domain.MyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyCardRepository extends JpaRepository<MyCard, Long> {
    Optional<List<MyCard>> findByAccountMemberId(Long memberId);
    Optional<MyCard> findByCardId(Long cardId);
}