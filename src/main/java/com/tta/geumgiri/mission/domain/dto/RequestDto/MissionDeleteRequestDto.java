package com.tta.geumgiri.mission.domain.dto.RequestDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MissionDeleteRequestDto {
    private Long userId; //유저 아이디
    private Long missionId; //미션 아이디
}
