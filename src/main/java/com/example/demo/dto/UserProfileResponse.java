package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String userName;
    private String fullName;
    private String password;
    private MultipartFile newAvatar;
    private String avatar;
}
