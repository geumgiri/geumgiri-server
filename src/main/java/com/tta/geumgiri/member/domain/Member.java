package com.tta.geumgiri.member.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tta.geumgiri.account.domain.Account;
import com.tta.geumgiri.common.entity.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.tta.geumgiri.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String userId;

    private String password;

    private String name;

    private int creditRatio;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // 직렬화 허용
    private List<Account> accounts = new ArrayList<>();


    @Builder
    public Member(String name, String userId, String password,Role role, List<Account> accounts) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.creditRatio = 500;
        this.role = role;
        this.accounts = accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts.addAll(accounts);
    }

}
