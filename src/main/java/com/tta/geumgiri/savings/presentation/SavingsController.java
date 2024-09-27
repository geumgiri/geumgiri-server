package com.tta.geumgiri.savings.presentation;

import com.tta.geumgiri.account.application.AccountService;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.common.exception.NotFoundException;
import com.tta.geumgiri.common.exception.BusinessException;
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
    @PostMapping("/create")
    public ResponseEntity<?> createSavings(
            @RequestParam Long memberId,
            @RequestParam String type,  // "deposit" 또는 "savings"로 구분
            @RequestParam Long amount,
            @RequestParam Double interestRate,
            @RequestParam int months,
            @RequestParam(required = false) Long monthlyDepositAmount,  // 적금일 경우에만 필요
            @RequestHeader("Authorization") String authHeader
    ) {
        if (type.equalsIgnoreCase("deposit")) {
            Deposit deposit = depositService.createDeposit(memberId, amount, interestRate, months, authHeader);
            return ResponseEntity.ok(deposit);
        } else if (type.equalsIgnoreCase("savings")) {
            if (monthlyDepositAmount == null) {
                throw new BusinessException(ErrorStatus.MISSING_MONTHLY_DEPOSIT);
            }
            Savings savings = savingsService.createSavings(memberId, monthlyDepositAmount, amount, interestRate, months, authHeader);
            return ResponseEntity.ok(savings);
        } else {
            throw new BusinessException(ErrorStatus.INVALID_SAVINGS_TYPE);
        }
    }


    // 해당 계좌의 모든 예금과 적금 조회 API
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getSavingsByAccountId(
            @RequestParam String accountNumber,
            @PathVariable Long memberId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String accessToken = authHeader.replace("Bearer ", "");
        Account account = accountService.getAccountByAccountNumber(accountNumber, memberId, accessToken);

        List<Deposit> deposits = depositService.getDepositsByAccountId(account);
        List<Savings> savingsList = savingsService.getSavingsByAccountId(account);

        return ResponseEntity.ok(new SavingsResponse(deposits, savingsList));
    }
}
