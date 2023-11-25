package com.example.demo.repo;

import com.example.demo.Entity.EducationProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProgramEducationRepo extends JpaRepository<EducationProgramEntity,Long> {

    Optional<EducationProgramEntity> findByName(String name);

    @Query(value = "select * from program join categoty_programs on program.id = categoty_programs.programs_id where categoty_programs.category_entity_id =:id",nativeQuery = true)
    List<EducationProgramEntity> findByCategory(@Param("id") Long id);

}
