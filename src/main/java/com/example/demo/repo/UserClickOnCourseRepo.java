package com.example.demo.repo;

import com.example.demo.Entity.UserClicksOnCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserClickOnCourseRepo extends JpaRepository<UserClicksOnCourseEntity,Long> {

    @Query(value = "select * from user_clicks_on_course_entity where user_id = :userId and course_id = :courseId",nativeQuery = true)
    UserClicksOnCourseEntity findUserClicksOnCourseEntities(@Param("userId")Long userId,@Param("courseId") Long courseId);
}
