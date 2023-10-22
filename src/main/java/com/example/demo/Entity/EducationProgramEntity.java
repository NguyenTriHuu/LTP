package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name ="program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    public EducationProgramEntity(String name, String code, Collection<SubjectEntity> subjects) {
        this.name = name;
        this.code = code;
        this.subjects = subjects;
    }

    @OneToMany
    private Collection<SubjectEntity> subjects =new ArrayList<>();

}
