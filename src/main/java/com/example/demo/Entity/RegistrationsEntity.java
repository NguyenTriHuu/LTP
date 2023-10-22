package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name ="registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime registrationDate;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private CourseEntity course;
}
