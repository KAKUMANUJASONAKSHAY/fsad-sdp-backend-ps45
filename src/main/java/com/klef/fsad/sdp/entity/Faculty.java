package com.klef.fsad.sdp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "faculty_table")
@Data
public class Faculty
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20, unique = true)
    private String contact;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(nullable = false, length = 50)
    private String designation;
}
