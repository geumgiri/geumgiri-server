package com.tta.geumgiri.mission.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MissionUpdateRequestDto {
    private Long userId; //유저 아이디
    private Long missionId; //미션 아이디
    private String missionTitle; //미션 이름
    private String description; //미션 내용
    private int reward; //보상
}
