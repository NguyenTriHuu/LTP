package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name ="interact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractEntity extends BaseEntity {
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private BlogEntity blog;
    @ManyToOne
    private CommentEntity comment;
}
