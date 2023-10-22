package com.example.demo.repo;

import com.example.demo.Entity.ThematicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThematicRepo extends JpaRepository<ThematicEntity,Long> {
}
