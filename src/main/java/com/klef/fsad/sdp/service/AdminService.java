package com.klef.fsad.sdp.service;

import java.util.List;
import java.util.Map;

import com.klef.fsad.sdp.dto.AchievementDTO;
import com.klef.fsad.sdp.dto.StudentDTO;
import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Student;

public interface AdminService
{
    // Student Management
    public List<Student> viewAllStudents();
    public String deleteStudent(int id);
    public StudentDTO studentToStudentDTO(Student s);
    public List<StudentDTO> displayAllStudentsDTO();

    // Achievement Management
    public String addAchievement(Achievement achievement);
    public String updateAchievementStatus(int achievementId, String status);
    public String deleteAchievement(int id);
    public List<Achievement> viewAllAchievements();
    public List<Achievement> viewAchievementsByStatus(String status);
    public AchievementDTO achievementToDTO(Achievement a);
    public List<AchievementDTO> displayAllAchievementsDTO();

    // Dashboard
    public Map<String, Long> getDashboardCounts();
}