package com.klef.fsad.sdp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.klef.fsad.sdp.entity.Achievement;
import com.klef.fsad.sdp.entity.AchievementFile;
import com.klef.fsad.sdp.repository.AchievementFileRepository;
import com.klef.fsad.sdp.repository.AchievementRepository;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
public class FileController
{
    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private AchievementFileRepository fileRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam int achievementId,
            @RequestParam("file") MultipartFile file)
    {
        try
        {
            Achievement achievement = achievementRepository.findById(achievementId)
                    .orElseThrow(() -> new RuntimeException("Achievement Not Found"));

            AchievementFile achievementFile = new AchievementFile();
            achievementFile.setFileName(file.getOriginalFilename());
            achievementFile.setFileType(file.getContentType());
            achievementFile.setFileData(file.getBytes());
            achievementFile.setAchievement(achievement);

            fileRepository.save(achievementFile);

            return ResponseEntity.ok("File Uploaded Successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("File Upload Failed: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable int fileId)
    {
        try
        {
            AchievementFile file = fileRepository.findById(fileId)
                    .orElseThrow(() -> new RuntimeException("File Not Found"));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(file.getFileData());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/achievement/{achievementId}")
    public ResponseEntity<List<AchievementFile>> viewFilesByAchievement(@PathVariable int achievementId)
    {
        return ResponseEntity.ok(fileRepository.findByAchievementId(achievementId));
    }
}
