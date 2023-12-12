package com.example.demo.repo;

import com.example.demo.Entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepo extends JpaRepository<RatingEntity,Long> {
    @Query(value = "select * from rating where user_id =:id and course_id is not null",nativeQuery = true)
    List<RatingEntity> findAllRateOfUserForCourse(@Param("id") Long id);

    @Query(value = "select * from rating where user_id =:idUser and course_id =:idCourse", nativeQuery = true)
    RatingEntity findRatingCourse(@Param("idCourse") Long idCourse, @Param("idUser") Long idUser);
}
