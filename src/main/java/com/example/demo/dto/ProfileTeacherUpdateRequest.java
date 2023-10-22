package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileTeacherUpdateRequest {
    private String teachingSubject;
    private String degree;
    private String workplace;
    private String linkFb;
    private String imageBg;
    private String email;
    private String contact;
    @JsonIgnore
    private MultipartFile imgBgFile;
    @JsonIgnore
    private MultipartFile avatarFile;
    @JsonIgnore
    private String fullName;
    @JsonIgnore
    private String passWord;
}
