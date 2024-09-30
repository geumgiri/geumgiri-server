package com.tta.geumgiri.savings.application.scheduler;

import com.tta.geumgiri.savings.application.SavingsService;
import com.tta.geumgiri.savings.domain.Savings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SavingsScheduler {

    private final SavingsService savingsService;

    public SavingsScheduler(SavingsService savingsService) {
        this.savingsService = savingsService;
    }

    // 트랜잭션을 적용하여 세션을 유지
    @Transactional
    @Scheduled(cron = "0 0/1 * * * ?")  // 1분마다 실행
    public void processMonthlySavings() {
        List<Savings> savingsList = savingsService.getAllSavings();
        StringBuilder messageBuilder = new StringBuilder();

        for (Savings savings : savingsList) {
            // 적금 만료 여부 체크
            if (savings.getEndDate().isBefore(java.time.LocalDateTime.now())) {
                messageBuilder.append("[적금] 사용자 ")
                        .append(savings.getAccount().getMember().getName())  // 사용자 이름
                        .append("이(가) 적금(").append(savings.getId())
                        .append(")을 완료했습니다: 총 적금 금액 ")
                        .append(savings.calculateTotalSavings()).append("원이 적립되었습니다.\n");
                continue;
            }

            try {
                // 적금 입금 처리
                savingsService.processMonthlySavings(savings);

                messageBuilder.append("사용자 ")
                        .append(savings.getAccount().getMember().getName())  // 사용자 이름
                        .append("이(가) 적금(").append(savings.getId())
                        .append(")에 입금하였습니다. 적금 횟수: ")
                        .append(savings.getMinutesElapsed()).append("번, 적금 총액: ")
                        .append(savings.calculateTotalSavings()).append("원입니다.\n");
            } catch (IllegalArgumentException e) {
                System.out.println("잔액 부족으로 적금 입금 실패: " + e.getMessage());
            }
        }

        // 턴이 끝날 때 구분선과 함께 출력
        System.out.println(messageBuilder.toString());
        System.out.println("-------------------------------------------------");
    }
}
