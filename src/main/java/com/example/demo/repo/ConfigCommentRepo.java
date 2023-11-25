package com.example.demo.repo;

import com.example.demo.Entity.ConfigCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigCommentRepo extends JpaRepository<ConfigCommentEntity,Long> {
    Optional<ConfigCommentEntity> findByConfig(String config);
}
