package com.example.demo.repo;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.dto.CategoryforSubjectRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.NamedNativeQuery;

public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {
    @Query(nativeQuery = true, name = "getCategoryBySubject")
    CategoryforSubjectRes findCategoryBySubject(Long id);

    @Query(value = "select count(*) from categoty \n" +
            "join categoty_programs on categoty.id = categoty_programs.category_entity_id \n" +
            "join program on  categoty_programs.programs_id =program.id \n" +
            "join program_subjects on program.id =program_subjects.education_program_entity_id \n" +
            "join subject on program_subjects.subjects_id=subject.id \n" +
            "join course  on subject.id = course.subject_id \n" +
            "where categoty.id =:id",nativeQuery = true)
    int countCourseByCategory (@Param("id") Long id);
}
