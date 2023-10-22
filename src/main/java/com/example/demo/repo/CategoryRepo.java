package com.example.demo.repo;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.dto.CategoryforSubjectRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.NamedNativeQuery;

public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {
    @Query(nativeQuery = true, name = "getCategoryBySubject")
    CategoryforSubjectRes findCategoryBySubject(Long id);


}
