package com.tta.geumgiri.account.application;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.domain.util.AccountRandomUtil;
import com.tta.geumgiri.account.persistence.AccountRepository;
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

    @Autowired
    public AccountService(AccountRepository accountRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
    }

    public Account createAccount(MemberResponse memberResponse, String accountName) {
        Member member = memberRepository.findById(memberResponse.id())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Account account = Account.builder()
                .accountName(accountName)
                .accountNumber(AccountRandomUtil.generateAccountNum(12))  // 계좌 번호 생성 메서드
                .member(member)
                .build();

        return accountRepository.save(account);
    }

    public List<Account> getAccountsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return member.getAccounts();
    }

    // 계좌 ID로 계좌 조회
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

}
