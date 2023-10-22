package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CourseDetailRequest {
    private List<ThematicRequest> thematics= new ArrayList<>();
}
