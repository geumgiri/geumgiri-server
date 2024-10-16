package com.tta.geumgiri.card.domain.dto;

import com.tta.geumgiri.card.domain.Benefit;
import com.tta.geumgiri.card.domain.BenefitCategory;
import com.tta.geumgiri.card.domain.BenefitType;
import lombok.Getter;

@Getter
public class BenefitResponse {

    private final BenefitType benefitType;
    private final BenefitCategory benefitCategory;
    private final Double value;

    public BenefitResponse(Benefit benefit) {
        this.benefitType = benefit.getBenefitType();
        this.benefitCategory = benefit.getBenefitCategory();
        this.value = benefit.getValue();
    }
}
