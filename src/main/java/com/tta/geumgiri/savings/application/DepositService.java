package com.tta.geumgiri.savings.application;

import com.tta.geumgiri.savings.domain.Deposit;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.savings.persistence.DepositRepository;
import com.tta.geumgiri.account.application.AccountService;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.common.exception.BusinessException;
import com.tta.geumgiri.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepositService {

    private final DepositRepository depositRepository;
    private final AccountService accountService;

    @Autowired
    public DepositService(DepositRepository depositRepository, AccountService accountService) {
        this.depositRepository = depositRepository;
        this.accountService = accountService;
    }

    public Deposit createDeposit(Long memberId, Long amount, Double interestRate, int months, String accessToken) {
        // 1. 회원의 계좌가 있는지 확인
        Account account = accountService.getAccountsByMemberId(memberId, accessToken).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ACCOUNT_NOT_FOUND));

        // 2. 계좌에서 돈을 출금 (잔액이 부족한 경우 예외 처리)
        if (account.getBalance() < amount) {
            throw new BusinessException(ErrorStatus.INSUFFICIENT_FUNDS);
        }
        account.deductBalance(amount);

        // 3. 새로운 예금 계좌 생성
        LocalDateTime depositDate = LocalDateTime.now();
        LocalDateTime maturityDate = depositDate.plusMonths(months);
        Deposit deposit = new Deposit(account, amount, depositDate, maturityDate, interestRate);

        return depositRepository.save(deposit);
    }

    public void processMaturedDeposit(Deposit deposit) {
        if (deposit.getMaturityDate().isBefore(LocalDateTime.now())) {
            Long maturedAmount = deposit.calculateMaturedAmount();
            deposit.getAccount().addBalance(maturedAmount);
            // 여기서 적절히 deposit을 만료 처리
        }
    }

    public List<Deposit> getDepositsByAccountId(Account account) {
        return depositRepository.findAllByAccount(account);
    }
}
