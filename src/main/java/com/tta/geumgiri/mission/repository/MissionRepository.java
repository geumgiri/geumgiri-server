package com.tta.geumgiri.mission.repository;

import com.tta.geumgiri.mission.domain.Mission;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission,Long> {
    //save
    //findById
    //findAll
    //deleteById 등 자동으로 제공

    List<Mission> findByUserId(Long userId);
}
