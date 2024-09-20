package com.tta.geumgiri.mission.service;

import com.tta.geumgiri.member.persistence.MemberRepository;
import com.tta.geumgiri.mission.domain.Mission;
import com.tta.geumgiri.mission.domain.dto.RequestDto.MissionCreateRequestDto;
import com.tta.geumgiri.mission.domain.dto.RequestDto.MissionDeleteRequestDto;
import com.tta.geumgiri.mission.domain.dto.RequestDto.MissionUpdateRequestDto;
import com.tta.geumgiri.mission.domain.dto.ResponseDto.MissionResponseDto;
import com.tta.geumgiri.mission.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MissionService {

    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;

    @Autowired
    public MissionService(MemberRepository memberRepository, MissionRepository missionRepository) {
        this.memberRepository = memberRepository;
        this.missionRepository = missionRepository;
    }

    public ResponseEntity<Map<String,Object>> createMission(MissionCreateRequestDto missionCreateRequestDto){
        //user id가 존재해야 함
        memberRepository.findById(missionCreateRequestDto.getUserId())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 유저입니다."));
        Long userId = missionCreateRequestDto.getUserId();
        String title = missionCreateRequestDto.getMissionTitle();
        String description = missionCreateRequestDto.getDescription();
        int reward = missionCreateRequestDto.getReward();
        Mission mission=new Mission(userId,title,description,reward);

        try{
            missionRepository.save(mission);
            Map<String, Object> response = new HashMap<>();
            response.put("status",MissionResponseDto.MISSION_CREATED.getStatus());
            response.put("message",MissionResponseDto.MISSION_CREATED.getMessage());
            response.put("data",Map.of(
                    "userId",mission.getUserId(),
                    "missionId",mission.getMissionId(),
                    "missionTitle",mission.getMissionTitle()
            ));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException("미션 생성 실패: "+e.getMessage());
        }
    }


    public ResponseEntity<Map<String,Object>> updateMission(MissionUpdateRequestDto missionUpdateRequestDto){
        memberRepository.findById(missionUpdateRequestDto.getUserId())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 유저입니다."));
        Mission mission=missionRepository.findById(missionUpdateRequestDto.getMissionId())
                        .orElseThrow(()-> new RuntimeException("해당 미션이 존재하지 않습니다."));
        mission.setMissionTitle(missionUpdateRequestDto.getMissionTitle());
        mission.setDescription(missionUpdateRequestDto.getDescription());
        mission.setReward(missionUpdateRequestDto.getReward());

        try{
            missionRepository.save(mission);

            Map<String, Object> response = new HashMap<>();
            response.put("status",MissionResponseDto.MISSION_UPDATED.getStatus();
            response.put("message","미션이 수정되었습니다.");
            response.put("data",Map.of(
                    "userId",mission.getUserId(),
                    "missionId",mission.getMissionId(),
                    "missionTitle",mission.getMissionTitle()
            ));
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e){
            throw new RuntimeException("미션 생성 실패: "+e.getMessage());
        }
    }

    public ResponseEntity<Map<String,Object>> deleteMission(MissionDeleteRequestDto missionDeleteRequestDto){
        memberRepository.findById(missionDeleteRequestDto.getUserId())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 유저입니다."));
        Mission mission=missionRepository.findById(missionDeleteRequestDto.getMissionId())
                .orElseThrow(()-> new RuntimeException("해당 미션이 존재하지 않습니다."));
        try{
            missionRepository.deleteById(missionDeleteRequestDto.getMissionId());

            Map<String, Object> response = new HashMap<>();
            response.put("status",MissionResponseDto.ALL_MISSIONS_FOUND.getStatus());
            response.put("message",MissionResponseDto.ALL_MISSIONS_FOUND.getMessage());
            response.put("data",Map.of(
                    "userId",missionDeleteRequestDto.getUserId(),
                    "missionId",missionDeleteRequestDto.getMissionId()
            ));
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e){
            throw new RuntimeException("미션 생성 실패: "+e.getMessage());
        }
    }

    public ResponseEntity<Map<String,Object>> findUserMission(Long missionId){
        try{
            List<Mission> missions =missionRepository.findByUserId(missionId);

            Map<String, Object> response = new HashMap<>();
            response.put("status",MissionResponseDto.USER_MISSIONS_FOUND.getStatus());
            response.put("message",MissionResponseDto.USER_MISSIONS_FOUND.getMessage());
            response.put("data",missions);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException("해당 유저의 미션이 존재하지 않습니다.: "+e.getMessage());
        }
    }

    public ResponseEntity<Map<String,Object>> findAllMission(){
        try{
            List<Mission> missions =missionRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status",MissionResponseDto.ALL_MISSIONS_FOUND.getStatus());
            response.put("message",MissionResponseDto.ALL_MISSIONS_FOUND.getMessage());
            response.put("data",missions);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException("미션이 존재하지 않습니다.: "+e.getMessage());
        }
    }


}
