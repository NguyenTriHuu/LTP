package com.example.demo.elasticsearch.controller;

import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.CourseResponse;
import com.example.demo.elasticsearch.SearchService.CourseElasticsearchService;
import com.example.demo.elasticsearch.SearchService.UserElasticsearchService;
import com.example.demo.elasticsearch.model.UserElasticsearchModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/search")
@RequiredArgsConstructor
@CrossOrigin
public class SearchControler {
    private final CourseElasticsearchService courseElasticsearchService;
    private final UserElasticsearchService userElasticsearchService;

    @GetMapping(value="/user/all")
    public ResponseEntity<List<UserElasticsearchModel>>  getAll(){
            return ResponseEntity.ok().body(userElasticsearchService.getAll());
    }

    @GetMapping(value="/user")
    public ResponseEntity<List<UserEntity>>  findUserByFilter(@RequestParam("search") String text){
        return ResponseEntity.ok().body(userElasticsearchService.findByFilter(text));
    }

    @GetMapping(value="/course")
    public ResponseEntity<List<CourseResponse>>  findCourseByFilter(@RequestParam("search") String text){
        return ResponseEntity.ok().body(courseElasticsearchService.findByFilter(text));
    }



}
