package com.tta.geumgiri.payment.domain.service;

import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.payment.domain.BenefitType;
import com.tta.geumgiri.payment.domain.strategy.*;
import jakarta.persistence.EntityNotFoundException;

public class BenefitStrategyFactory {
    public static BenefitStrategy getStrategy(BenefitType benefitType) {
        return switch (benefitType) {
            case DISCOUNT -> new DiscountBenefitStrategy();
            case FIXED_POINT -> new FixedPointBenefitStrategy();
            case PERCENTAGE_POINT -> new PercentagePointBenefitStrategy();
            case CASHBACK -> new CashbackBenefitStrategy();
            default -> throw new EntityNotFoundException(String.valueOf(ErrorStatus.BENEFIT_NOT_FOUND));
        };
    }
}