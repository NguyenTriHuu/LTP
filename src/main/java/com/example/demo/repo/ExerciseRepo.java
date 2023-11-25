package com.example.demo.repo;

import com.example.demo.Entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseRepo extends JpaRepository<ExerciseEntity ,Long > {

    @Modifying
    @Query(value = "delete from exercise where id =:id",nativeQuery = true)
    void deleteById (@Param("id") Long id);

    @Modifying
    @Query(value = "delete from lecture_exercises where exercises_id =:id",nativeQuery = true)
    void deleteByLecture (@Param("id") Long id);

    @Modifying
    @Query(value = "delete from choice where exercise_id =:id",nativeQuery = true)
    void deleteByChoice (@Param("id") Long id);

    @Modifying
    @Query(value = "delete from answer where exercise_id =:id",nativeQuery = true)
    void deleteByAnswer (@Param("id") Long id);
}
