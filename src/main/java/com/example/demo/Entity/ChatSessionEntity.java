package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="chatsession")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private CourseEntity course;

    @OneToMany
    private List<ChatMessageEntity> messages = new ArrayList<>();
}
