package com.example.demo.service;

import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.Entity.CourseEntity;
import com.example.demo.dto.CourseDetailRequest;
import com.example.demo.dto.CourseSaveRequest;
import com.example.demo.dto.CourseUpdateRequest;
import com.example.demo.dto.ListCourseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface CourseService {
    CourseEntity findCourseById(Long id);
    CourseEntity save(CourseSaveRequest course);
    List<CourseEntity> findAll();

    void addThematictoCourse(long idCourse,long idThematic);

    void saveCourseDetail(Long id,CourseDetailRequest courseDetailRequest);

    ListCourseResponse findAllByFilter (Map<String, Object> params);

    CourseEntity update(CourseUpdateRequest courseUpdateRequest) throws JsonProcessingException;

    byte[] downloadThumdnail(Long id);

    S3Object downloadVideo(Long id);

    String getVideoUrl(Long id);
}
