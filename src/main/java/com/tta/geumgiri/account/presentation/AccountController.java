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
    public ResponseEntity<Account> createAccount(@RequestParam Long memberId,
                                                 @RequestParam String accountName,
                                                 @RequestHeader("Authorization") String authHeader) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String accessToken = authHeader.replace("Bearer ", "");

        MemberResponse member = memberService.getMember(memberId);
        if (member == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Account newAccount = accountService.createAccount(member, accountName, accessToken);

        return ResponseEntity.ok(newAccount);
    }

    // 해당 멤버의 전체 계좌 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Account>> getAccountsByMemberId(@PathVariable Long memberId,
                                                               @RequestHeader("Authorization") String authHeader) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String accessToken = authHeader.replace("Bearer ", "");

        List<Account> accounts = accountService.getAccountsByMemberId(memberId, accessToken);


        if (accounts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accounts);
    }


    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber,
                                                             @PathVariable Long memberId,
                                                             @RequestHeader("Authorization") String authHeader) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String accessToken = authHeader.replace("Bearer ", "");

        Account account = accountService.getAccountByAccountNumber(accountNumber, memberId, accessToken);
        return ResponseEntity.ok(account);
    }

}
