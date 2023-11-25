package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name ="course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity extends BaseEntity {
    @Column (columnDefinition = "TEXT")
    private String title;
    private String image;
    @Column (columnDefinition = "TEXT")
    private String description;
    @Column (columnDefinition = "TEXT")
    private String shortDescription;
    private int price;
    private LocalDateTime dateStart;
    private boolean status;
    private int duration;
    private String linkVideoIntro;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;

    @OneToMany
    @JoinColumn(name = "course_id")
    private Collection<ThematicEntity> thematics =new ArrayList<>();

}
