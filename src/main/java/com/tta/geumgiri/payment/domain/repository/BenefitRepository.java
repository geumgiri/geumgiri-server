package com.tta.geumgiri.payment.domain.repository;

import com.tta.geumgiri.payment.domain.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
}
