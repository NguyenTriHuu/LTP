package com.example.demo.repo;

import com.example.demo.Entity.EducationProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ProgramEducationRepo extends JpaRepository<EducationProgramEntity,Long> {

    Optional<EducationProgramEntity> findByName(String name);


}
