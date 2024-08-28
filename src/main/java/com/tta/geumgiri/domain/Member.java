package com.tta.geumgiri.domain;

import jakarta.persistence.*;

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    @ManyToOne
    private String account;

    // 신용 등급
    private int creditRating;

    public Member(String name, String Account, int creditRating) {
        this.name = name;
        this.account = account;
        this.creditRating = creditRating;
    }

    public static Member create(String name, String account, int creditRating) {
        return new Member(name, account, creditRating);
    }

}
