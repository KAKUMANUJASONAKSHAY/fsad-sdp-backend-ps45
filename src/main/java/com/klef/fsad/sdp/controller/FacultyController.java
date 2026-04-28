package com.klef.fsad.sdp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.Student;
import com.klef.fsad.sdp.service.FacultyService;

@RestController
@RequestMapping("facultyapi")
public class FacultyController
{
    @Autowired
    private FacultyService facultyService;

    @GetMapping("/")
    public String index()
    {
        return "Faculty Controller - Achievement Review";
    }

    @GetMapping("/viewdepartmentstudents")
    public ResponseEntity<List<Student>> viewDepartmentStudents(@RequestParam String department)
    {
        return ResponseEntity.ok(facultyService.viewDepartmentStudents(department));
    }

    @GetMapping("/viewdepartmentachievements")
    public ResponseEntity<List<Achievement>> viewDepartmentAchievements(@RequestParam String department)
    {
        return ResponseEntity.ok(facultyService.viewDepartmentAchievements(department));
    }

    @GetMapping("/viewpendingachievements")
    public ResponseEntity<List<Achievement>> viewPendingAchievements(@RequestParam String department)
    {
        return ResponseEntity.ok(facultyService.viewPendingDepartmentAchievements(department));
    }

    @PostMapping("/reviewachievement")
    public ResponseEntity<String> reviewAchievement(
            @RequestParam int achievementId,
            @RequestParam String facultyStatus,
            @RequestParam String facultyRemarks)
    {
        String output = facultyService.reviewAchievement(achievementId, facultyStatus, facultyRemarks);
        return ResponseEntity.ok(output);
    }
}
