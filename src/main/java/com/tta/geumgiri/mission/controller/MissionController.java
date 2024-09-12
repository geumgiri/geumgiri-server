package com.tta.geumgiri.mission.controller;

import com.tta.geumgiri.mission.domain.dto.MissionCreateRequestDto;
import com.tta.geumgiri.mission.domain.dto.MissionDeleteRequestDto;
import com.tta.geumgiri.mission.domain.dto.MissionUpdateRequestDto;
import com.tta.geumgiri.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mission")
public class MissionController {
    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMision(@RequestBody MissionCreateRequestDto missionCreateRequestDto){
        missionService.createMission(missionCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("미션이 생성되었습니다.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMision(@RequestBody MissionUpdateRequestDto missionUpdateRequestDto){
        missionService.updateMission(missionUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("미션이 업데이트 되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMision(@RequestBody MissionDeleteRequestDto missionDeleteRequestDto){
        missionService.deleteMission(missionDeleteRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("미션이 삭제되었습니다.");
    }

//    @GetMapping("/find")
//    public ResponseEntity<String> findMision(@RequestBody Long missionId){
//        missionService.findMission(missionId);
//        return ResponseEntity.status(HttpStatus.CREATED).body("성공");
//    }
}
