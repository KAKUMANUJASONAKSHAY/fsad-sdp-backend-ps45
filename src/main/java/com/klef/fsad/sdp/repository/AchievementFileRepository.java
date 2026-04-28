package com.klef.fsad.sdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.fsad.sdp.entity.AchievementFile;

@Repository
public interface AchievementFileRepository extends JpaRepository<AchievementFile, Integer>
{
    List<AchievementFile> findByAchievementId(int achievementId);
}
