package com.tta.geumgiri.mission.service;

import com.tta.geumgiri.member.persistence.MemberRepository;
import com.tta.geumgiri.mission.domain.Mission;
import com.tta.geumgiri.mission.domain.dto.MissionCreateRequestDto;
import com.tta.geumgiri.mission.domain.dto.MissionDeleteRequestDto;
import com.tta.geumgiri.mission.domain.dto.MissionUpdateRequestDto;
import com.tta.geumgiri.mission.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MissionService {

    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;

    @Autowired
    public MissionService(MemberRepository memberRepository, MissionRepository missionRepository) {
        this.memberRepository = memberRepository;
        this.missionRepository = missionRepository;
    }

    public Mission createMission(MissionCreateRequestDto missionCreateRequestDto){
        //user id가 존재해야 함
        memberRepository.findById(missionCreateRequestDto.getUserId())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 유저입니다."));
        Long userId = missionCreateRequestDto.getUserId();
        String title = missionCreateRequestDto.getMissionTitle();
        String description = missionCreateRequestDto.getDescription();
        int reward = missionCreateRequestDto.getReward();
        Mission mission=new Mission(userId,title,description,reward);

        try{
            return missionRepository.save(mission);
        }catch (Exception e){
            throw new RuntimeException("미션 생성 실패: "+e.getMessage());
        }
    }


    public Mission updateMission(MissionUpdateRequestDto missionUpdateRequestDto){
        memberRepository.findById(missionUpdateRequestDto.getUserId())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 유저입니다."));
        Mission mission=missionRepository.findById(missionUpdateRequestDto.getMissionId())
                        .orElseThrow(()-> new RuntimeException("해당 미션이 존재하지 않습니다."));
        mission.setMissionTitle(missionUpdateRequestDto.getMissionTitle());
        mission.setDescription(missionUpdateRequestDto.getDescription());
        mission.setReward(missionUpdateRequestDto.getReward());

        try{
            return missionRepository.save(mission);
        }catch (Exception e){
            throw new RuntimeException("미션 생성 실패: "+e.getMessage());
        }
    }

    public void deleteMission(MissionDeleteRequestDto missionDeleteRequestDto){
        memberRepository.findById(missionDeleteRequestDto.getUserId())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 유저입니다."));
        Mission mission=missionRepository.findById(missionDeleteRequestDto.getMissionId())
                .orElseThrow(()-> new RuntimeException("해당 미션이 존재하지 않습니다."));
        try{
            missionRepository.deleteById(missionDeleteRequestDto.getMissionId());
        }catch (Exception e){
            throw new RuntimeException("미션 생성 실패: "+e.getMessage());
        }
    }

//    public Mission findMission(Long missionId){
//        Mission mission=missionRepository.findById(missionId)
//                .orElseThrow(()-> new RuntimeException("해당 미션이 존재하지 않습니다."));
//        try{
//            return mission;
//        }catch (Exception e){
//            throw new RuntimeException("미션 생성 실패: "+e.getMessage());
//        }
//    }


}
