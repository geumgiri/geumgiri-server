package com.tta.geumgiri.member.controller;

import com.tta.geumgiri.member.domain.dto.AddMemberRequest;
import com.tta.geumgiri.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/user")
    public String signup(@ModelAttribute AddMemberRequest request, Model model) {
        try {
            memberService.save(request);
            return "redirect:/login";
        } catch (MemberService.EmailAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup"; // 이메일 중복이 있을 경우, 다시 회원가입 페이지로 돌아가도록 함
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
