package com.tta.geumgiri.member.persistence;

import com.tta.geumgiri.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByName(String username);

  Optional<Member> findByUserId(String userId);
}
