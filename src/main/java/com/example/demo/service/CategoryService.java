package com.example.demo.service;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.dto.CategoryResponse;
import com.example.demo.dto.CategoryforSubjectRes;
import com.example.demo.repo.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    List<CategoryResponse> finAllForTree();

    void save(CategoryEntity categoryEntity);

    void addProgramToCategory (long idCategory,long idProgram);

    CategoryforSubjectRes findCategoryBySubject(Long idSub);
}
