package com.example.demo.api.web;


import com.example.demo.dto.CategoryResponse;
import com.example.demo.dto.CategoryforSubjectRes;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class CategoryApi {
    private final CategoryService categoryService;
    @GetMapping("/category/tree")
    public ResponseEntity<List<CategoryResponse>> getAllCategory(){
        return ResponseEntity.ok().body(categoryService.finAllForTree());
    }
    @GetMapping("/category")
    public ResponseEntity<CategoryforSubjectRes> getAllCategory(@RequestParam Long idSubject){
        return ResponseEntity.ok().body(categoryService.findCategoryBySubject(idSubject));
    }

}
