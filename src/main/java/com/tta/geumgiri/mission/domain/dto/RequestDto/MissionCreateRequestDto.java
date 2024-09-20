package com.tta.geumgiri.mission.domain.dto.RequestDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter //lombok을 gradle에 추가 후, Plugin 설치
public class MissionCreateRequestDto {
    private Long userId;
    private String missionTitle; //미션 이름
    private String description; //미션 내용
    private int reward; //보상
}
