package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name ="subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;


    @OneToMany(mappedBy = "subject")
    private Collection<BlogEntity> blogs =new ArrayList<>();

    public SubjectEntity(String name, String code, Collection<CourseEntity> courses, Collection<BlogEntity> blogs) {
        this.name = name;
        this.code = code;
        this.blogs = blogs;
    }
}
