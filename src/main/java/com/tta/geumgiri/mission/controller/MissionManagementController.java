package com.tta.geumgiri.mission.controller;

import com.tta.geumgiri.mission.domain.dto.MissionCreateRequestDto;
import com.tta.geumgiri.mission.service.MissionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mission")
public class MissionManagementController {
    private final MissionManagementService missionManagementService;

    @Autowired
    public MissionManagementController(MissionManagementService missionManagementService) {
        this.missionManagementService = missionManagementService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMision(@RequestBody MissionCreateRequestDto missionCreateRequestDto){
        missionManagementService.createMission(missionCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("성공");
    }
}
