package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name ="exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseEntity extends BaseEntity{
    private String type;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(columnDefinition = "TEXT")
    private String solution;
    private Boolean locked;

    @OneToMany
    @JoinColumn(name = "exercise_id")
    private Collection<AnswerEntity> answers =new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "exercise_id")
    private Collection<ChoiceEntity> choices =new ArrayList<>();




}
