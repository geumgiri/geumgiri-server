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

    @PostMapping("/apply")
    public ResponseEntity<Loan> applyForLoan(@RequestParam Long memberId,
                                             @RequestParam Long amount,
                                             @RequestParam String accountNumber,
                                             @RequestParam String repaymentDate) {
        // 대출 신청
        Loan loan = loanService.applyForLoan(memberId, amount, accountNumber, LocalDate.parse(repaymentDate));
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }
}
