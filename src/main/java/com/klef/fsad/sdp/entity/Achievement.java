package com.klef.fsad.sdp.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "achievement_table")
@Data
public class Achievement
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, length = 50)
    private String category; // e.g. Sports, Arts, Tech, Cultural

    @Column(nullable = false, length = 50)
    private String level; // e.g. College, State, National, International

    @Column(nullable = false)
    private LocalDate achievementDate;

    @Column(length = 255)
    private String certificateUrl;

    @Column(nullable = false, length = 20)
    private String status; // e.g. PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

   
}