package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {
    private String nameCategoryResponse;
    private String codeCategory;
    private List<ProgramEducationResponse> educationResponseList;
}
