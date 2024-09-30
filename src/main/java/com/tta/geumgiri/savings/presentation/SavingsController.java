package com.tta.geumgiri.savings.presentation;

import com.tta.geumgiri.account.application.AccountService;
import com.tta.geumgiri.account.domain.Account;
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

    private final SavingsService savingsService;
    private final AccountService accountService;

    @Autowired
    public SavingsController(SavingsService savingsService, AccountService accountService) {
        this.savingsService = savingsService;
        this.accountService = accountService;
    }

    // 예금 또는 적금 생성 API
    @PostMapping("/create")
    public ResponseEntity<?> createSavingsOrDeposit(
            @RequestParam Long memberId,
            @RequestParam String type,  // "deposit" 또는 "savings"로 구분
            @RequestParam Long amount,
            @RequestParam Double interestRate,
            @RequestParam int minutes,
            @RequestParam(required = false) Long monthlyDepositAmount,  // 적금일 경우에만 필요
            @RequestHeader("Authorization") String authHeader
    ) {
        String accessToken = authHeader.replace("Bearer ", "");

        Object result = savingsService.createSavingsOrDeposit(memberId, amount, interestRate, minutes, type, accessToken, monthlyDepositAmount);
        return ResponseEntity.ok(result);
    }

    // 해당 계좌의 모든 예금과 적금 조회 API
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getSavingsAndDepositsByAccountId(
            @RequestParam String accountNumber,
            @PathVariable Long memberId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String accessToken = authHeader.replace("Bearer ", "");
        Account account = accountService.getAccountByAccountNumber(accountNumber, memberId, accessToken);

        List<Deposit> deposits = savingsService.getDepositsByAccountId(account);
        List<Savings> savingsList = savingsService.getSavingsByAccountId(account);

        return ResponseEntity.ok(new SavingsResponse(deposits, savingsList));
    }
}