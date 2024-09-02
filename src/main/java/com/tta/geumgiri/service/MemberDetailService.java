package com.tta.geumgiri.service;


import com.tta.geumgiri.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    //스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
    private final MemberRepository memberRepository;

    // 사용자 이름(email)로 사용자 정보를 가져오는 메소드
    @Override
    public UserDetails loadUserByUsername(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }


}
