package com.tta.geumgiri.mission.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "missions")
@ToString
@Getter
@Setter
public class Mission {
    @Id
    @Column(name = "mission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//1씩 증가하며 자동생성
    private Long missionId; //미션 아이디

    @Column(name = "mission_title",nullable = false)
    private String missionTitle; //미션 이름
    @Column(name = "description")
    private String description; //미션 내용
    @Column(name = "reward",nullable = false)
    private int reward; //보상
    @Column(name = "mission_state")
    private boolean missionState; //보상 여부

    @Builder //객체 생성
    public Mission(String title,String description, int reward){
        this.missionTitle=title;
        this.description=description;
        this.reward=reward;
        this.missionState=false;
    }

}

