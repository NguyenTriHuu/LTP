package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name ="thematic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThematicEntity extends BaseEntity{

    private String name;
    private Boolean locked;

    @OneToMany
    private Collection<LectureEntity> lectures = new ArrayList<>();

}
