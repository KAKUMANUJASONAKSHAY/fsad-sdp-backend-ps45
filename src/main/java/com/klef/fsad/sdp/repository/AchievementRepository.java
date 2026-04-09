package com.klef.fsad.sdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klef.fsad.sdp.entity.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer>
{
    List<Achievement> findByStudentId(int studentId);

    List<Achievement> findByStatus(String status);

    List<Achievement> findByCategory(String category);

    List<Achievement> findByLevel(String level);

    @Query("SELECT a FROM Achievement a WHERE a.student.id = ?1 AND a.status = ?2")
    List<Achievement> findByStudentIdAndStatus(int studentId, String status);

    @Query("SELECT COUNT(a) FROM Achievement a WHERE a.status = ?1")
    long countByStatus(String status);

    @Query("SELECT COUNT(a) FROM Achievement a WHERE a.student.id = ?1")
    long countByStudentId(int studentId);

    @Query("SELECT COUNT(a) FROM Achievement a WHERE a.category = ?1")
    long countByCategory(String category);
}