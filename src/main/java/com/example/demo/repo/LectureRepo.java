package com.example.demo.repo;

import com.example.demo.Entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepo extends JpaRepository<LectureEntity,Long> {
}
