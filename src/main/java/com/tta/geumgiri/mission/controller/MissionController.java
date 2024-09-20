package com.tta.geumgiri.mission.controller;

import com.tta.geumgiri.mission.domain.dto.RequestDto.MissionCreateRequestDto;
import com.tta.geumgiri.mission.domain.dto.RequestDto.MissionDeleteRequestDto;
import com.tta.geumgiri.mission.domain.dto.RequestDto.MissionUpdateRequestDto;
import com.tta.geumgiri.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mission")
public class MissionController {
    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String,Object>> createMision(@RequestBody MissionCreateRequestDto missionCreateRequestDto){
        return missionService.createMission(missionCreateRequestDto);
        //ResponseEntity.status(HttpStatus.CREATED).body("미션이 생성되었습니다.");
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String,Object>> updateMision(@RequestBody MissionUpdateRequestDto missionUpdateRequestDto){
        return missionService.updateMission(missionUpdateRequestDto);
        //ResponseEntity.status(HttpStatus.CREATED).body("미션이 업데이트 되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String,Object>> deleteMision(@RequestBody MissionDeleteRequestDto missionDeleteRequestDto){
        return missionService.deleteMission(missionDeleteRequestDto);
        //ResponseEntity.status(HttpStatus.CREATED).body("미션이 삭제되었습니다.");
    }

    @GetMapping("/findUserMission")
    public ResponseEntity<Map<String,Object>> findMision(@RequestBody Long missionId){
        return missionService.findUserMission(missionId);
        //ResponseEntity.status(HttpStatus.CREATED).body("성공");
    }

    @GetMapping("/findAllMission")
    public ResponseEntity<Map<String,Object>> findMision(){
        return missionService.findAllMission();
        //ResponseEntity.status(HttpStatus.CREATED).body("성공");
    }
}
