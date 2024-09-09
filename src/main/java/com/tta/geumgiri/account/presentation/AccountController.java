package com.tta.geumgiri.account.presentation;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.application.AccountService;
import com.tta.geumgiri.member.presentation.dto.response.MemberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.tta.geumgiri.member.application.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final MemberService memberService;

    @Autowired
    public AccountController(AccountService accountService, MemberService memberService) {
        this.accountService = accountService;
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestParam Long memberId) {
        MemberResponse member = memberService.getMember(memberId);
        if (member == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Account newAccount = accountService.createAccount(member);

        return ResponseEntity.ok(newAccount);
    }
}
