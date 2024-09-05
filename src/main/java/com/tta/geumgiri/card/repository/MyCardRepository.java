package com.tta.geumgiri.card.repository;

import com.tta.geumgiri.card.domain.MyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCardRepository extends JpaRepository<MyCard, Long> {
}