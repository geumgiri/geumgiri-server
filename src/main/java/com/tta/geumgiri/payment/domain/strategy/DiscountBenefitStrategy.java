package com.tta.geumgiri.payment.domain.strategy;

import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.payment.domain.Benefit;

public class DiscountBenefitStrategy implements BenefitStrategy {

    @Override
    public void applyBenefit(Long paymentAmount, Benefit benefit, MyCard myCard) {
        // 할인 금액 계산 및 처리
        Long discountAmount = (Long) Math.round(paymentAmount * (benefit.getValue() / 100.0));
        paymentAmount -= discountAmount;
    }
}