package com.example.demo.repo;

import com.example.demo.Entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepo extends JpaRepository<AnswerEntity ,Long> {
    @Modifying
    @Query(value = "delete from answer where exercise_id =:id", nativeQuery = true)
    void deleteALLByIdEx (@Param("id")Long id);

    @Query(value = "select count(*) from answer where choice_id =:idChoice",nativeQuery = true)
    int ChoiceIsCorrect (@Param("idChoice") Long id);
}
