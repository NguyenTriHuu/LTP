package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    private String userName;
    private Long idCourse;
    private Integer rating;
    private Long idBlog;
    private Long idRating;
}
