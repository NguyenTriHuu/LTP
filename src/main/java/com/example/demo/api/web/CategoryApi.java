package com.example.demo.api.web;


import com.example.demo.Entity.CategoryEntity;
import com.example.demo.dto.CategoryRequest;
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

    @GetMapping("/category/all")
    public ResponseEntity<List<CategoryEntity>> getAllCategorys(){
        return ResponseEntity.ok().body(categoryService.getAll());
    }

    @PostMapping(value = "/category/save")
    public CategoryEntity save (@RequestBody CategoryRequest categoryRequest){
        if(categoryRequest.getId()!=null)
            return categoryService.update(categoryRequest);
       return categoryService.save(categoryRequest.getName(), categoryRequest.getCode());
    }

    @PutMapping(value = "/category/update")
    public void update (@RequestBody CategoryRequest categoryRequest){
        categoryService.update(categoryRequest);
    }

    @PostMapping(value = "/category/check")
    public ResponseEntity<String> checkCategory(@RequestBody Long[] ids){
        return ResponseEntity.ok().body(categoryService.checkCategory(ids));
    }

}
