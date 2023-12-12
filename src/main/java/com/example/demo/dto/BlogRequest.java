package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {
    private Long id;
    private String title;
    private String content;
    private Long idCategory;
    private String configComment;
    private String userName;
    private String shortDescription;
    private MultipartFile thumbnail;
}
