package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name ="study")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyEntity {
    @EmbeddedId
    private StudyKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "class_id")
    private CourseEntity course;

}
