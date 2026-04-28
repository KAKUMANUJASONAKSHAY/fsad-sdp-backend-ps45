package com.klef.fsad.sdp.service;

import java.util.List;

import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Faculty;
import com.klef.fsad.sdp.entity.Student;

public interface FacultyService
{
    public String facultyRegistration(Faculty faculty);
    public List<Student> viewDepartmentStudents(String department);
    public List<Achievement> viewDepartmentAchievements(String department);
    public List<Achievement> viewPendingDepartmentAchievements(String department);
    public String reviewAchievement(int achievementId, String facultyStatus, String facultyRemarks);
}
