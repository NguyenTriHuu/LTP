package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CourseUpdateRequest {
    private Long id;
    private String title;
    private MultipartFile file;
    private String description;
    private Integer price;
    private String subject;
    private LocalDateTime dateTime;
    private String thematics;
    private Boolean status;
    private String shortDescription;
    private MultipartFile thumnail;
}
