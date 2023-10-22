package com.example.demo.repo;

import com.example.demo.Entity.RoleEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<RoleEntity,Long> {
    RoleEntity findByName(String name);
}
