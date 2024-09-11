package com.tta.geumgiri.mission.service;

import com.tta.geumgiri.mission.domain.Mission;
import com.tta.geumgiri.mission.domain.dto.MissionCreateRequestDto;
import com.tta.geumgiri.mission.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

@Service
public class MissionManagementService {

    private final MissionRepository missionRepository;

    @Autowired
    public MissionManagementService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public Mission createMission(MissionCreateRequestDto missionCreateRequestDto){
        String title = missionCreateRequestDto.getMissionTitle();
        String description = missionCreateRequestDto.getDescription();
        int reward = missionCreateRequestDto.getReward();
        Mission mission=new Mission(title,description,reward);

        try{
            return missionRepository.save(mission);
        }catch (Exception e){
            throw new RuntimeException("미션 생성 실패: "+e.getMessage());
        }
    }


}
