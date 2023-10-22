package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserClicksOnCourseEntity {
    @EmbeddedId
    UserClickOnCourseKey id;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    CourseEntity course;

    int value;
}
