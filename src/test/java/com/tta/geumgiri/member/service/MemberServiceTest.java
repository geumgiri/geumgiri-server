package com.tta.geumgiri.member.service;

import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.domain.dto.AddMemberRequest;
import com.tta.geumgiri.member.repository.MemberRepository;
import com.tta.geumgiri.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    // Mock 객체를 정의합니다. 실제 구현체가 아닌 가짜 객체를 생성하여 테스트에 사용합니다.
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 테스트 대상 객체를 정의합니다. @InjectMocks annotatioin을 사용하여 mock 객체가 주입됩니다.
    @InjectMocks
    private MemberService memberService;

    // 각 테스트 메서드 실행 전 호출됩니다. 테스트 환경을 준비합니다.
    @BeforeEach
    void setUp() {
        System.out.println("## test ready ##");
        System.out.println();
        // Mockito의 mock 객체와 @InjectMocks 애너테이션을 처리합니다.
        MockitoAnnotations.openMocks(this);
    }

    // 각 테스트 메서드 실행 후 호출됩니다. 테스트 후 정리 작업을 수행합니다.
    @AfterEach
    void fin() {
        System.out.println("## test finished ##");
        System.out.println();
    }

    // 새로운 멤버를 저장하는 기능을 테스트합니다.
    @Test
    @DisplayName("새로운 멤버 저장하기 테스트")
    void testSaveNewMember() {
        // Arrange: 테스트에 필요한 객체와 상태를 설정합니다.
        AddMemberRequest request = new AddMemberRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        // Create a mock member: Member 객체의 mock을 생성합니다.
        Member newMember = mock(Member.class);
        // Mocking: mock 객체의 getId() 메서드 호출 시 1L을 반환하도록 설정합니다.
        when(newMember.getId()).thenReturn(1L);

        // Mock repository behavior: MemberRepository의 동작을 설정합니다.
        // 이메일로 회원을 찾을 때 빈 Optional을 반환하도록 설정합니다.
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        // 비밀번호를 인코딩할 때 "encodedPassword"를 반환하도록 설정합니다.
        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        // MemberRepository의 save 메서드가 호출될 때 mock 객체를 반환하도록 설정합니다.
        when(memberRepository.save(any(Member.class))).thenReturn(newMember);

        // Act: 실제 메서드를 호출하여 행동을 실행합니다.
        Long memberId = memberService.save(request);

        // Assert: 결과를 검증합니다.
        // memberId가 null이 아닌지 확인합니다.
        assertNotNull(memberId);
        // 반환된 memberId가 1L과 동일한지 확인합니다.
        assertEquals(1L, memberId);

        // Verify: save 메서드가 호출되었는지 검증합니다.
        verify(memberRepository).save(any(Member.class));

        // Print success message: 테스트가 성공했음을 콘솔에 출력합니다.
        System.out.println("Test for saving a new member succeeded!");
        System.out.println();

    }

    // 이미 존재하는 이메일로 회원가입을 시도하는 경우를 테스트합니다.
    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입하기 테스트")
    void testSaveMemberWithExistingEmail() {
        // Arrange: 테스트에 필요한 객체와 상태를 설정합니다.
        AddMemberRequest request = new AddMemberRequest();
        request.setEmail("test1@example.com");
        request.setPassword("password123");

        // Mocking repository behavior: MemberRepository의 동작을 설정합니다.
        // 이미 존재하는 이메일로 회원을 찾을 때 mock 객체를 반환하도록 설정합니다.
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mock(Member.class)));

        // Act & Assert: 메서드를 호출하고 예외를 검증합니다.
        // EmailAlreadyExistsException 예외가 발생하는지 확인합니다.
        Exception exception = assertThrows(MemberService.EmailAlreadyExistsException.class, () -> {
            memberService.save(request);
        });

        // 발생한 예외의 메시지가 예상과 일치하는지 확인합니다.
        assertEquals("Email is already taken.", exception.getMessage());

        // Print success message: 테스트가 성공했음을 콘솔에 출력합니다.
        System.out.println("Test for saving member with existing email succeeded!");
        System.out.println();
    }
}
