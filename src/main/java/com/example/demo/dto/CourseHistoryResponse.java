package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseHistoryResponse {
    private Long idCourse;
    private String title;
    private LocalDateTime registrationDate;
    private Integer price;
}
