package com.tta.geumgiri.loan.persistence;

import com.tta.geumgiri.loan.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByRepaymentDate(LocalDate repaymentDate); // 상환 날짜로 대출 조회
    List<Loan> findByMemberId(Long memberId); // 회원의 대출 조회
}
