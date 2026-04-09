package com.klef.fsad.sdp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "admin_table")
@Data
public class Admin
{
    @Id
    @Column(length = 50)
    private String username;

    @Column(length = 50, nullable = false)
    private String password;

    
}