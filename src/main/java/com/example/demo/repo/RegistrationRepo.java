package com.example.demo.repo;

import com.example.demo.Entity.RegistrationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepo extends JpaRepository<RegistrationsEntity,Long> {

    //@Query("SELECT r FROM RegistrationsEntity r WHERE r.course.id = :courseId AND r.role.id = :roleId")
    @Query(value = "select * from registration where course_id = :courseId and role_id =:roleId",nativeQuery = true)
    Optional<RegistrationsEntity> findByCourseAndRole(@Param("courseId") Long courseId, @Param("roleId") Long roleId);

    @Query(value = "select * from registration where course_id = :courseId and user_id =:userId and role_id =:roleId",nativeQuery = true)
    Optional<RegistrationsEntity> findByUserAndCourse(@Param("courseId") Long courseId, @Param("userId") Long userId, @Param("roleId") Long roleId);
}
