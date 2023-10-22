package com.example.demo.repo;

import com.example.demo.Entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<FileEntity,Long> {
}
