package com.example.demo.repo;

import com.example.demo.Entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepo extends JpaRepository<PermissionEntity,Long> {
    PermissionEntity findByName(String name);
}
