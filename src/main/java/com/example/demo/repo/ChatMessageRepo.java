package com.example.demo.repo;

import com.example.demo.Entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepo extends JpaRepository<ChatMessageEntity,Long> {
}
