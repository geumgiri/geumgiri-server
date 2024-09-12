package com.tta.geumgiri.account.application;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.domain.util.AccountRandomUtil;
import com.tta.geumgiri.account.persistence.AccountRepository;
import com.tta.geumgiri.auth.application.AuthService;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.persistence.MemberRepository;
import com.tta.geumgiri.member.presentation.dto.response.MemberResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AuthService authService;

    @Autowired
    public AccountService(AccountRepository accountRepository, MemberRepository memberRepository, AuthService authService, AuthService authService1) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
        this.authService = authService1;
    }

    public Account createAccount(MemberResponse memberResponse, String accountName, String accessToken) {

        authService.validateUserAccess(memberResponse.id(),accessToken);

        Member member = memberRepository.findById(memberResponse.id())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.MEMBER_NOT_FOUND)));
        Account account = Account.builder()
                .accountName(accountName)
                .accountNumber(AccountRandomUtil.generateAccountNum(12))  // 계좌 번호 생성 메서드
                .member(member)
                .build();

        return accountRepository.save(account);
    }

    public List<Account> getAccountsByMemberId(Long memberId, String accessToken) {
        authService.validateUserAccess(memberId,accessToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.MEMBER_NOT_FOUND)));
        return member.getAccounts();
    }

    public Account getAccountByAccountNumber(String accountNumber, Long memberId, String accessToken) {
        authService.validateUserAccess(memberId,accessToken);
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.UNAUTHORIZED_USER)));

    }

//    public Account depositMoney(){
//
//    }
//
//    public Account withdrawMoney(){
//
//    }

}
