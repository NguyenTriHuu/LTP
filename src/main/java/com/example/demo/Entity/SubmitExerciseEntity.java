package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name ="submit_exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String submit;

    @ManyToOne
    private UserEntity user;
}
