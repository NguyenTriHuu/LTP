package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private String approved;
    private String shortDescription;
    private Long id;
    private String image;
    private int price;
    private boolean status;
    private String title;
    private String avatarTeacher;
    private String fullNameTeacher;
    private String userName;
    private byte[] avatar;
    private Long idTeacher;
    public CourseResponse(String approved, String shortDescription, Long id, String image, int price, boolean status, String title, String avatarTeacher, String fullNameTeacher, String userName) {
        this.approved = approved;
        this.shortDescription = shortDescription;
        this.id = id;
        this.image = image;
        this.price = price;
        this.status = status;
        this.title = title;
        this.avatarTeacher = avatarTeacher;
        this.fullNameTeacher = fullNameTeacher;
        this.userName = userName;
    }

    public CourseResponse(String shortDescription, Long id, int price, boolean status, String title) {
        this.shortDescription = shortDescription;
        this.id = id;
        this.price = price;
        this.status = status;
        this.title = title;
    }
}
