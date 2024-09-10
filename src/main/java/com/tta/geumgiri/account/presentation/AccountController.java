package com.tta.geumgiri.account.presentation;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.application.AccountService;
import com.tta.geumgiri.member.presentation.dto.response.MemberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.tta.geumgiri.member.application.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final MemberService memberService;

    @Autowired
    public AccountController(AccountService accountService, MemberService memberService) {
        this.accountService = accountService;
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(
            @RequestParam Long memberId,
            @RequestParam String accountName
    ) {
        MemberResponse member = memberService.getMember(memberId);
        if (member == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Account newAccount = accountService.createAccount(member, accountName);

        return ResponseEntity.ok(newAccount);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Account>> getAccountsByMemberId(@PathVariable Long memberId) {
        List<Account> accounts = accountService.getAccountsByMemberId(memberId);
        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(accounts);
    }

}
