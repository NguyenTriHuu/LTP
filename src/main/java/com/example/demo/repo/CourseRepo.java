package com.example.demo.repo;

import com.example.demo.Entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<CourseEntity,Long> {
        Optional<CourseEntity> findById(Long id);
        CourseEntity findByTitle(String name);
        @Query(value =
                "select course.id, course.title, course.createdtime,course.date_start,course.description,course.image,course.modifiedtime,course.price,course.status,course.subject_id, course.duration , course.short_description ,course.link_video_intro " +
                "from categoty join categoty_programs on categoty.id = categoty_programs.category_entity_id \n" +
                "join program on  categoty_programs.programs_id =program.id \n" +
                "join program_subjects on program.id =program_subjects.education_program_entity_id \n" +
                "join subject on program_subjects.subjects_id=subject.id \n" +
                "join course  on subject.id = course.subject_id \n" +
                "where LOWER(categoty.code) like lower(?1)  and lower(program.code) like lower(?2) and lower(subject.code) like lower(?3)" +
                "and lower(course.title) like lower(?4) and course.status =?5 order by course.date_start desc limit ?6,?7",nativeQuery = true)
        List<CourseEntity> findAllByFilter(String category,
                                           String program,
                                           String subject,
                                           String stringQuery,
                                           boolean status,
                                           int offSet,
                                           int page);

        @Query(value = "select * from course where title like %?1% order by title desc limit 1,2",nativeQuery = true)
        List<CourseEntity> test(String title);
}
