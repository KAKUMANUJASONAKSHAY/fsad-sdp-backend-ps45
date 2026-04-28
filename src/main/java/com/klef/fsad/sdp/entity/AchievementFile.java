package com.klef.fsad.sdp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "achievement_file_table")
@Data
public class AchievementFile
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fileName;

    private String fileType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;
}
