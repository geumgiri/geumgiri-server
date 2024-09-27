package com.tta.geumgiri.savings.application.scheduler;


import com.tta.geumgiri.savings.application.SavingsService;
import com.tta.geumgiri.savings.domain.Savings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SavingsScheduler {

    private final SavingsService savingsService;

    public SavingsScheduler(SavingsService savingsService) {
        this.savingsService = savingsService;
    }

    // 매월 1일에 적금 입금 처리
    @Scheduled(cron = "0 0 0 1 * *")
    public void processMonthlySavings() {
        List<Savings> savingsList = savingsService.getAllSavings();
        for (Savings savings : savingsList) {
            savingsService.processMonthlySavings(savings);
        }
    }
}
