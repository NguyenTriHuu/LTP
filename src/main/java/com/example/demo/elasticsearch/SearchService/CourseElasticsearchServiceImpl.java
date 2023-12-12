package com.example.demo.elasticsearch.SearchService;

import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.CourseResponse;
import com.example.demo.elasticsearch.Repository.CourseElasticsearchRepo;
import com.example.demo.elasticsearch.model.CourseElasticsearchModel;
import com.example.demo.elasticsearch.model.UserElasticsearchModel;
import com.example.demo.repo.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseElasticsearchServiceImpl implements CourseElasticsearchService{
    private final CourseElasticsearchRepo courseElasticsearchRepo;
    private final CourseRepo courseRepo;
    @Override
    public List<CourseResponse> findByFilter(String text) {

        return null;
    }
}
