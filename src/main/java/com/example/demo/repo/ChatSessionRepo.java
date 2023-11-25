package com.example.demo.repo;

import com.example.demo.Entity.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepo extends JpaRepository<ChatSessionEntity,Long> {
}
