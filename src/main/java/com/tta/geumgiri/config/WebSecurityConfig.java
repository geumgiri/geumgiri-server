package com.tta.geumgiri.config;

import com.tta.geumgiri.member.service.MemberDetailService;
import com.tta.geumgiri.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/static/**").permitAll() // static 리소스 접근 허용
                        .requestMatchers("/login", "/signup", "/user").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/articles")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, MemberService memberService, MemberDetailService memberDetailService)
            throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authManagerBuilder.userDetailsService(memberDetailService)
                .passwordEncoder(bCryptPasswordEncoder);

        return authManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
