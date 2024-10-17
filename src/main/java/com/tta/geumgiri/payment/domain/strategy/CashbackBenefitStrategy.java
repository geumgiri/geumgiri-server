package com.tta.geumgiri.payment.domain.strategy;

import com.tta.geumgiri.card.domain.MyCard;
import com.tta.geumgiri.payment.domain.Benefit;

public class CashbackBenefitStrategy implements BenefitStrategy {

    @Override
    public void applyBenefit(Long paymentAmount, Benefit benefit, MyCard myCard) {
        // 캐시백 처리: 연결된 계좌에 금액 추가
        Long cashbackAmount = (Long) Math.round(paymentAmount * (benefit.getValue() / 100.0));
        myCard.getAccount().addBalance(cashbackAmount);
    }
}
