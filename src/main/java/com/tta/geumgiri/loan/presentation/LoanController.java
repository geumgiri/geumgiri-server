package com.tta.geumgiri.loan.presentation;

import com.tta.geumgiri.loan.domain.Loan;
import com.tta.geumgiri.loan.application.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // 대출 신청
    @PostMapping("/apply")
    public ResponseEntity<Loan> applyForLoan(@RequestParam Long memberId,
                                             @RequestParam Long amount,
                                             @RequestParam String accountNumber,
                                             @RequestParam String repaymentDate,
                                             @RequestParam int installments) {
        Loan loan = loanService.applyForLoan(memberId, amount, accountNumber, LocalDate.parse(repaymentDate), installments);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    // 대출 상환 요청
    @PostMapping("/repay")
    public ResponseEntity<String> repayLoan(@RequestParam Long loanId) {
        try {
            loanService.payInstallment(loanId);
            return ResponseEntity.ok("상환이 성공적으로 처리되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상환 실패: " + e.getMessage());
        }
    }


}
