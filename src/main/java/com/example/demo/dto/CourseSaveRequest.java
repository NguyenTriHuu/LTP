package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CourseSaveRequest {
    private String title;
    private MultipartFile file;
    private String description;
    private Integer price;
    private String userName;
    private String subject;
    private LocalDateTime dateTime;
    private String shortDescription;
    private MultipartFile thumdnail;
}
