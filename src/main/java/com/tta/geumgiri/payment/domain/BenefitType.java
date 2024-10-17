package com.tta.geumgiri.payment.domain;

public enum BenefitType {
    DISCOUNT,        // 결제 시 % 할인
    FIXED_POINT,     // 고정된 포인트 지급
    PERCENTAGE_POINT, // 결제 금액의 일정 % 포인트 지급
    CASHBACK         // 결제 후 캐시백 지급
}