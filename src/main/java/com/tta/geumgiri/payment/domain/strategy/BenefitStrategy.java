package com.tta.geumgiri.payment.domain.strategy;

import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.payment.domain.Benefit;

public interface BenefitStrategy {
    void applyBenefit(Long paymentAmount, Benefit benefit, MyCard myCard);
}