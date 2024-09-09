package com.tta.geumgiri.account.application;

import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.account.persistence.AccountRepository;
import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.persistence.MemberRepository;
import com.tta.geumgiri.member.presentation.dto.response.MemberResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
    }

    public void createAccount(MemberResponse memberResponse) {
        Member member = memberRepository.findById(memberResponse.getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Account account = new Account();
        account.setMember(member);
        // Save the account entity
    }
}
