package com.klef.fsad.sdp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klef.fsad.sdp.dto.AchievementDTO;
import com.klef.fsad.sdp.dto.StudentDTO;
import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Faculty;
import com.klef.fsad.sdp.entity.Student;
import com.klef.fsad.sdp.exception.ResourceNotFoundException;
import com.klef.fsad.sdp.repository.AchievementRepository;
import com.klef.fsad.sdp.repository.FacultyRepository;
import com.klef.fsad.sdp.repository.StudentRepository;

@Service
public class AdminServiceImpl implements AdminService
{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Student> viewAllStudents()
    {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty())
        {
            throw new ResourceNotFoundException("No Students Found");
        }
        return students;
    }

    @Override
    public String deleteStudent(int id)
    {
        studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student Not Found"));
        studentRepository.deleteById(id);
        return "Student Deleted Successfully";
    }

    @Override
    public StudentDTO studentToStudentDTO(Student s)
    {
        StudentDTO dto = new StudentDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setEmail(s.getEmail());
        dto.setDepartment(s.getDepartment());
        dto.setRollNumber(s.getRollNumber());
        dto.setGender(s.getGender());
        return dto;
    }

    @Override
    public List<StudentDTO> displayAllStudentsDTO()
    {
        List<Student> students = viewAllStudents();
        return students.stream()
                .map(this::studentToStudentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String addFaculty(Faculty faculty)
    {
        if (faculty.getPassword() != null && !faculty.getPassword().isEmpty())
        {
            faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
        }

        facultyRepository.save(faculty);
        return "Faculty Added Successfully";
    }

    @Override
    public List<Faculty> viewAllFaculty()
    {
        return facultyRepository.findAll();
    }

    @Override
    public String deleteFaculty(int id)
    {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty Not Found"));

        facultyRepository.delete(faculty);
        return "Faculty Deleted Successfully";
    }

    @Override
    public String addAchievement(Achievement achievement)
    {
        achievement.setStatus("APPROVED");
        achievementRepository.save(achievement);
        return "Achievement Added Successfully";
    }

    @Override
    public String updateAchievementStatus(int achievementId, String status)
    {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement Not Found"));
        achievement.setStatus(status);
        achievementRepository.save(achievement);
        return "Achievement Status Updated to " + status;
    }

    @Override
    public String deleteAchievement(int id)
    {
        achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement Not Found"));
        achievementRepository.deleteById(id);
        return "Achievement Deleted Successfully";
    }

    @Override
    public List<Achievement> viewAllAchievements()
    {
        List<Achievement> list = achievementRepository.findAll();
        if (list.isEmpty())
        {
            throw new ResourceNotFoundException("No Achievements Found");
        }
        return list;
    }

    @Override
    public List<Achievement> viewAchievementsByStatus(String status)
    {
        return achievementRepository.findByStatus(status);
    }

    @Override
    public AchievementDTO achievementToDTO(Achievement a)
    {
        AchievementDTO dto = new AchievementDTO();
        dto.setId(a.getId());
        dto.setTitle(a.getTitle());
        dto.setCategory(a.getCategory());
        dto.setLevel(a.getLevel());
        dto.setAchievementDate(a.getAchievementDate());
        dto.setStatus(a.getStatus());
        dto.setStudentName(a.getStudent().getName());
        dto.setRollNumber(a.getStudent().getRollNumber());
        dto.setDepartment(a.getStudent().getDepartment());
        return dto;
    }

    @Override
    public List<AchievementDTO> displayAllAchievementsDTO()
    {
        List<Achievement> achievements = achievementRepository.findAll();
        return achievements.stream()
                .map(this::achievementToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getDashboardCounts()
    {
        Map<String, Long> data = new HashMap<>();
        data.put("studentCount", studentRepository.count());
        data.put("totalAchievements", achievementRepository.count());
        data.put("approvedCount", achievementRepository.countByStatus("APPROVED"));
        data.put("pendingCount", achievementRepository.countByStatus("PENDING"));
        data.put("rejectedCount", achievementRepository.countByStatus("REJECTED"));
        return data;
    }
}
