package com.klef.fsad.sdp.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
public class AchievementDTO
{
    private int id;
    private String title;
    private String category;
    private String level;
    private LocalDate achievementDate;
    private String status;
    private String studentName;
    private String rollNumber;
    private String department;

    
}