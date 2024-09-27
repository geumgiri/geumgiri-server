package com.tta.geumgiri.savings.presentation;

import com.tta.geumgiri.account.application.AccountService;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.savings.application.DepositService;
import com.tta.geumgiri.savings.application.SavingsService;
import com.tta.geumgiri.savings.domain.Deposit;
import com.tta.geumgiri.savings.domain.Savings;
import com.tta.geumgiri.savings.domain.dto.SavingsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/savings")
public class SavingsController {

    private final DepositService depositService;
    private final SavingsService savingsService;
    private final AccountService accountService;

    @Autowired
    public SavingsController(DepositService depositService, SavingsService savingsService, AccountService accountService) {
        this.depositService = depositService;
        this.savingsService = savingsService;
        this.accountService = accountService;
    }

    // 예금 또는 적금 생성 API
    @PostMapping("/{memberId}/create")
    public ResponseEntity<?> createSavings(@PathVariable Long memberId,
                                           @RequestParam String type,  // "deposit" 또는 "savings"로 구분
                                           @RequestParam Long amount,
                                           @RequestParam Double interestRate,
                                           @RequestParam int months,
                                           @RequestParam(required = false) Long monthlyDepositAmount,  // 적금일 경우에만 필요
                                           @RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.replace("Bearer ", "");

        Account account = accountService.getAccountsByMemberId(memberId, accessToken).stream()
                .findFirst()
                .orElse(null);

        if (account == null) {
            return ResponseEntity.badRequest().body("해당 멤버의 계좌가 존재하지 않습니다.");
        }

        if (type.equalsIgnoreCase("deposit")) {
            Deposit deposit = depositService.createDeposit(account, amount, interestRate, months);
            return ResponseEntity.ok(deposit);
        } else if (type.equalsIgnoreCase("savings")) {
            if (monthlyDepositAmount == null) {
                return ResponseEntity.badRequest().body("적금에는 매월 납입금액이 필요합니다.");
            }
            Savings savings = savingsService.createSavings(account, monthlyDepositAmount, interestRate, months);
            return ResponseEntity.ok(savings);
        } else {
            return ResponseEntity.badRequest().body("잘못된 타입입니다. 'deposit' 또는 'savings' 중 하나를 선택해주세요.");
        }
    }

    // 해당 계좌의 모든 예금과 적금 조회 API
    @GetMapping("/{accountNumber}/{memberId}")
    public ResponseEntity<?> getSavingsByAccountId(@PathVariable String accountNumber,
                                                   @PathVariable Long memberId,
                                                   @RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.replace("Bearer ", "");
        Account account = accountService.getAccountByAccountNumber(accountNumber, memberId, accessToken);

        List<Deposit> deposits = depositService.getDepositsByAccountId(account);
        List<Savings> savingsList = savingsService.getSavingsByAccountId(account);

        return ResponseEntity.ok(new SavingsResponse(deposits, savingsList));
    }
}
