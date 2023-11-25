package com.example.demo.repo;

import com.example.demo.Entity.ChoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChoiceRepo extends JpaRepository<ChoiceEntity , Long> {

    @Modifying
    @Query(value = "delete from answer where choice_id =:id",nativeQuery = true)
    void deleteByAnswer (@Param("id") Long id);

    @Modifying
    @Query(value = "delete from choice where id =:id",nativeQuery = true)
    void deleteById (@Param("id") Long id);


}
