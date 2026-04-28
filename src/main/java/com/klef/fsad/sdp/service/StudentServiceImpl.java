package com.klef.fsad.sdp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Student;
import com.klef.fsad.sdp.exception.ResourceNotFoundException;
import com.klef.fsad.sdp.repository.AchievementRepository;
import com.klef.fsad.sdp.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService
{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String studentRegistration(Student student)
    {
        String encodedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(encodedPassword);
        studentRepository.save(student);
        return "Student Registered Successfully";
    }

    @Override
    public String updateStudentProfile(Student student)
    {
        Optional<Student> optional = studentRepository.findById(student.getId());

        if (optional.isPresent())
        {
            Student s = optional.get();
            s.setName(student.getName());
            s.setContact(student.getContact());
            s.setDepartment(student.getDepartment());

            if (student.getPassword() != null && !student.getPassword().isEmpty())
            {
                s.setPassword(passwordEncoder.encode(student.getPassword()));
            }

            studentRepository.save(s);
            return "Student Profile Updated Successfully";
        }
        else
        {
            return "Student ID Not Found";
        }
    }

    @Override
    public List<Achievement> viewMyAchievements(int studentId)
    {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student Not Found"));

        List<Achievement> achievements = achievementRepository.findByStudentId(studentId);

        if (achievements.isEmpty())
        {
            throw new ResourceNotFoundException("No Achievements Found for this Student");
        }

        return achievements;
    }

    @Override
    public List<Achievement> viewMyAchievementsByCategory(int studentId, String category)
    {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student Not Found"));

        return achievementRepository.findByStudentIdAndCategory(studentId, category);
    }

    @Override
    public Achievement submitAchievement(Achievement achievement)
    {
        achievement.setStatus("PENDING");
        return achievementRepository.save(achievement);
    }
}
