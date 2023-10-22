package com.example.demo.repo;

import com.example.demo.Entity.SubjectEntity;
import com.example.demo.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepo extends JpaRepository< SubjectEntity,Long> {

    Optional<SubjectEntity> findByName(String name);
    Optional<SubjectEntity> findByCode(String code);
}
