package com.example.demo.Entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StudyKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "class_id")
    private Long courseId;
}
