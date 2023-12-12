package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private String linkThumnail;
    private Boolean status;
    @Column(columnDefinition = "TEXT")
    private String shortDescription;
    private LocalDateTime dateCreate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
