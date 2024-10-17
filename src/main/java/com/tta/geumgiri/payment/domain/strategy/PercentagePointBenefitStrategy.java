package com.tta.geumgiri.payment.domain.strategy;

import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.payment.domain.Benefit;

public class PercentagePointBenefitStrategy implements BenefitStrategy {

    @Override
    public void applyBenefit(Long paymentAmount, Benefit benefit, MyCard myCard) {
        Long points = (Long) Math.round(paymentAmount * (benefit.getValue() / 100.0));
        myCard.addCardPoints(points);
    }
}