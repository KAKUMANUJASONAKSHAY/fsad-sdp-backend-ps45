package com.klef.fsad.sdp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.sdp.dto.AchievementDTO;
import com.klef.fsad.sdp.dto.StudentDTO;
import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Student;
import com.klef.fsad.sdp.service.AdminService;

@RestController
@RequestMapping("adminapi")
@CrossOrigin("*")
public class AdminController
{
    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String index(){
        return "Student Extracurricular Achievement Platform - Admin";
    }

    // -------------------- STUDENT MANAGEMENT --------------------

    @GetMapping("/viewallstudents")
    public ResponseEntity<?> viewAllStudents(){
        try{
            List<Student> students = adminService.viewAllStudents();
            if (students.size() > 0)
                return ResponseEntity.ok().body(students);
            else
                return ResponseEntity.noContent().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error Fetching Students");
        }
    }

    @GetMapping("/viewallstudentsdto")
    public ResponseEntity<?> viewAllStudentsDTO()
    {
        try
        {
            List<StudentDTO> students = adminService.displayAllStudentsDTO();
            return ResponseEntity.ok(students);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error Fetching Students");
        }
    }

    @DeleteMapping("/deletestudent")
    public ResponseEntity<String> deleteStudent(@RequestParam int id)
    {
        try
        {
            String output = adminService.deleteStudent(id);
            return ResponseEntity.status(200).body(output);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // -------------------- ACHIEVEMENT MANAGEMENT --------------------

    @PostMapping("/addachievement")
    public ResponseEntity<String> addAchievement(@RequestBody Achievement achievement)
    {
        try
        {
            String output = adminService.addAchievement(achievement);
            return ResponseEntity.status(201).body(output);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/updateachievementstatus")
    public ResponseEntity<String> updateAchievementStatus(@RequestParam int id, @RequestParam String status)
    {
        try
        {
            String output = adminService.updateAchievementStatus(id, status);
            return ResponseEntity.status(200).body(output);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @DeleteMapping("/deleteachievement/{id}")
    public ResponseEntity<String> deleteAchievement(@PathVariable int id)
    {
        try
        {
            String output = adminService.deleteAchievement(id);
            return ResponseEntity.ok(output);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/viewallachievements")
    public ResponseEntity<?> viewAllAchievements()
    {
        try
        {
            List<Achievement> achievements = adminService.viewAllAchievements();
            if (achievements.size() > 0)
                return ResponseEntity.ok(achievements);
            else
                return ResponseEntity.noContent().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error Fetching Achievements");
        }
    }

    @GetMapping("/viewachievementsbystatus")
    public ResponseEntity<?> viewAchievementsByStatus(@RequestParam String status)
    {
        try
        {
            List<Achievement> achievements = adminService.viewAchievementsByStatus(status);
            if (achievements.size() > 0)
                return ResponseEntity.ok(achievements);
            else
                return ResponseEntity.noContent().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error Fetching Achievements");
        }
    }

    @GetMapping("/viewallachievementsdto")
    public ResponseEntity<?> viewAllAchievementsDTO()
    {
        try
        {
            List<AchievementDTO> achievements = adminService.displayAllAchievementsDTO();
            return ResponseEntity.ok(achievements);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error Fetching Achievements");
        }
    }

    // -------------------- DASHBOARD --------------------

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboard()
    {
        return ResponseEntity.ok(adminService.getDashboardCounts());
    }
}