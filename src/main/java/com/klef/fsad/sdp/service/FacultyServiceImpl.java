package com.klef.fsad.sdp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Faculty;
import com.klef.fsad.sdp.entity.Student;
import com.klef.fsad.sdp.exception.ResourceNotFoundException;
import com.klef.fsad.sdp.repository.AchievementRepository;
import com.klef.fsad.sdp.repository.FacultyRepository;
import com.klef.fsad.sdp.repository.StudentRepository;

@Service
public class FacultyServiceImpl implements FacultyService
{
    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String facultyRegistration(Faculty faculty)
    {
        faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
        facultyRepository.save(faculty);
        return "Faculty Registered Successfully";
    }

    @Override
    public List<Student> viewDepartmentStudents(String department)
    {
        return studentRepository.findByDepartment(department);
    }

    @Override
    public List<Achievement> viewDepartmentAchievements(String department)
    {
        return achievementRepository.findByStudentDepartment(department);
    }

    @Override
    public List<Achievement> viewPendingDepartmentAchievements(String department)
    {
        return achievementRepository.findByStudentDepartmentAndStatus(department, "PENDING");
    }

    @Override
    public String reviewAchievement(int achievementId, String facultyStatus, String facultyRemarks)
    {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement Not Found"));

        achievement.setFacultyStatus(facultyStatus);
        achievement.setFacultyRemarks(facultyRemarks);

        achievementRepository.save(achievement);
        return "Achievement Reviewed by Faculty";
    }
}
