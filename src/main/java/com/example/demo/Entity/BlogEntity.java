package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name ="blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogEntity extends BaseEntity{
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;

    @OneToMany(mappedBy = "blog")
    private Collection<CommentEntity> comments =new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
