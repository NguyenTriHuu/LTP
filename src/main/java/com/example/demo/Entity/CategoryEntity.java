package com.example.demo.Entity;

import com.example.demo.dto.CategoryforSubjectRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name ="categoty")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
        name = "CategoryResult",
        classes = {
                @ConstructorResult(
                        targetClass = CategoryforSubjectRes.class,
                        columns = {
                                @ColumnResult(name = "nameCategory", type = String.class),
                                @ColumnResult(name = "nameProgram", type = String.class),
                                @ColumnResult(name = "nameSubject", type = String.class)
                        }
                )
        }
)
@NamedNativeQuery(name ="getCategoryBySubject",
        query =  "select categoty.name as nameCategory,program.name as nameProgram ,subject.name as nameSubject " +
        "from categoty  join categoty_programs on categoty.id = categoty_programs.category_entity_id " +
        "join program  on  categoty_programs.programs_id = program.id " +
        "join program_subjects on program.id=program_subjects.education_program_entity_id " +
        "join subject on program_subjects.subjects_id=subject.id where subject.id =?1",
        resultSetMapping = "CategoryResult"
)
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    public CategoryEntity(String name, String code, Collection<EducationProgramEntity> programs) {
        this.name = name;
        this.code = code;
        this.programs = programs;
    }

    @OneToMany
    private Collection<EducationProgramEntity> programs =new ArrayList<>();

}
