package com.example.demo.repo;

import com.example.demo.Entity.SubjectEntity;
import com.example.demo.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepo extends JpaRepository< SubjectEntity,Long> {

    Optional<SubjectEntity> findByName(String name);
    Optional<SubjectEntity> findByCode(String code);

    @Query(value = "select education_program_entity_id from program_subjects where subjects_id =:id",nativeQuery = true)
    Long getProgramBySubject (@Param("id") Long id);

    @Query(value = "select * from subject join program_subjects on subject.id = program_subjects.subjects_id where program_subjects.education_program_entity_id =:id order by subject.id desc",nativeQuery = true)
    List<SubjectEntity> getAllByProgram (@Param("id") Long id);
}
