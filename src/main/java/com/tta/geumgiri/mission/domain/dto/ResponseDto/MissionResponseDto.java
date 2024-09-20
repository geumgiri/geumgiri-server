package com.tta.geumgiri.mission.domain.dto.ResponseDto;

import org.springframework.http.HttpStatus;

public enum MissionResponseDto {//에러코드
    MISSION_CREATED(HttpStatus.CREATED.value(), "미션이 성공적으로 생성되었습니다."),
    MISSION_UPDATED(HttpStatus.OK.value(), "미션이 수정되었습니다."),
    MISSION_DELETED(HttpStatus.OK.value(), "미션이 삭제되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."),
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 미션이 존재하지 않습니다."),
    USER_MISSIONS_FOUND(HttpStatus.OK.value(), "유저의 모든 미션 조회 성공"),
    ALL_MISSIONS_FOUND(HttpStatus.OK.value(), "모든 미션 조회");

    private final int status;
    private final String message;

    MissionResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
