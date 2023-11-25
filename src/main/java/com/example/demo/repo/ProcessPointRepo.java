package com.example.demo.repo;

import com.example.demo.Entity.ProcessPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProcessPointRepo extends JpaRepository<ProcessPointEntity,Long> {

    @Query(value = "select * from process_point where course_id =:idCourse and user_id= :idUser" , nativeQuery = true)
    Optional<ProcessPointEntity> findByCourseAndUser(@Param("idCourse") Long idCourse,@Param("idUser") Long idUser);
}
