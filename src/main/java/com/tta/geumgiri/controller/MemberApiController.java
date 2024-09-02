package com.tta.geumgiri.controller;

import com.tta.geumgiri.domain.dto.AddMemberRequest;
import com.tta.geumgiri.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/login")
    public String signup(AddMemberRequest request){
        memberService.save(request);
        return "redirect:/login";
    }


}