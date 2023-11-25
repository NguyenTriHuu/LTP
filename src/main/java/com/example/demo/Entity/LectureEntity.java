package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name ="lecture")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureEntity extends BaseEntity{
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private boolean locked;
    private String linkVideo;
    private String code;

    @OneToMany
    private Collection<ExerciseEntity> exercises = new ArrayList<>();

    @OneToMany
    private Collection<FileEntity> files = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "configcomment_id")
    private ConfigCommentEntity configComment;
}
