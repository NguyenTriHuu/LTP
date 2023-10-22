package com.example.demo.dto;

import com.example.demo.Entity.CourseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListCourseResponse {
    List<CourseEntity> listCourse=new ArrayList<>();
    Integer previousPage;
    Integer nextPage;

}
