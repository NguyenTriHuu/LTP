package com.example.demo.elasticsearch.SearchService;

import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.CourseResponse;

import java.util.List;

public interface CourseElasticsearchService {
    List<CourseResponse> findByFilter(String text);
}
