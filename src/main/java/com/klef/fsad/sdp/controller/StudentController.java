package com.klef.fsad.sdp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Student;
import com.klef.fsad.sdp.exception.ResourceNotFoundException;
import com.klef.fsad.sdp.service.StudentService;

@RestController
@RequestMapping("studentapi")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String index()
    {
        return "Student Controller - Extracurricular Achievement Platform";
    }

    @PostMapping("/registration")
    public ResponseEntity<String> studentRegistration(@RequestBody Student student)
    {
        try
        {
            String output = studentService.studentRegistration(student);
            return ResponseEntity.status(201).body(output);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/updateprofile")
    public ResponseEntity<String> updateProfile(@RequestBody Student student)
    {
        try
        {
            String output = studentService.updateStudentProfile(student);
            return ResponseEntity.status(200).body(output);
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/viewmyachievements/{studentId}")
    public ResponseEntity<?> viewMyAchievements(@PathVariable int studentId)
    {
        try
        {
            List<Achievement> achievements = studentService.viewMyAchievements(studentId);

            if (achievements == null || achievements.isEmpty())
            {
                return ResponseEntity.status(204).body("No Achievements Found");
            }

            return ResponseEntity.ok(achievements);
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error Fetching Achievements");
        }
    }

    @GetMapping("/viewachievementsbycategory/{studentId}")
    public ResponseEntity<?> viewByCategory(@PathVariable int studentId, @RequestParam String category)
    {
        try
        {
            List<Achievement> achievements = studentService.viewMyAchievementsByCategory(studentId, category);

            if (achievements == null || achievements.isEmpty())
            {
                return ResponseEntity.status(204).body("No Achievements Found for this Category");
            }

            return ResponseEntity.ok(achievements);
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error Fetching Achievements");
        }
    }

    @PostMapping("/submitachievement")
    public ResponseEntity<Achievement> submitAchievement(@RequestBody Achievement achievement)
    {
        try
        {
            Achievement output = studentService.submitAchievement(achievement);
            return ResponseEntity.status(201).body(output);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }
    }
}
