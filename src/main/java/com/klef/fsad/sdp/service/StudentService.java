package com.klef.fsad.sdp.service;

import java.util.List;

import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Student;

public interface StudentService
{
    public String studentRegistration(Student student);
    public String updateStudentProfile(Student student);
    public List<Achievement> viewMyAchievements(int studentId);
    public List<Achievement> viewMyAchievementsByCategory(int studentId, String category);
    public String submitAchievement(Achievement achievement);
}