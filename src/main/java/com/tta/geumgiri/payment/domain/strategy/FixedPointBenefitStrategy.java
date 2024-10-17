package com.tta.geumgiri.payment.domain.strategy;

import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.payment.domain.Benefit;

public class FixedPointBenefitStrategy implements BenefitStrategy {

    @Override
    public void applyBenefit(Long paymentAmount, Benefit benefit, MyCard myCard) {
        Long points = benefit.getValue();
        myCard.addCardPoints(points);
    }
}
