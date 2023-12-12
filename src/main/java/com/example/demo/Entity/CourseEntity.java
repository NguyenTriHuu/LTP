package com.example.demo.Entity;

import com.example.demo.dto.CategoryforSubjectRes;
import com.example.demo.dto.CourseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name ="course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
        name = "CourseResult",
        classes = {
                @ConstructorResult(
                        targetClass = CourseResponse.class,
                        columns = {
                                @ColumnResult(name = "approved", type = String.class),
                                @ColumnResult(name = "shortDescription", type = String.class),
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "image", type = String.class),
                                @ColumnResult(name = "price", type = Integer.class),
                                @ColumnResult(name = "status", type = Boolean.class),
                                @ColumnResult(name = "title", type = String.class),
                                @ColumnResult(name = "avatarTeacher", type = String.class),
                                @ColumnResult(name = "fullNameTeacher", type = String.class)
                        }
                )
        }
)
@NamedNativeQuery(name ="getCourseResponse",
        query =  "select course.approved,course.short_description as shortDescription,course.id, course.image, course.price,course.status,course.title,user.avatar as avatarTeacher, user.fullname as fullNameTeacher from course join registration on course.id = registration.course_id left join profile_teacher on registration.user_id = profile_teacher.id join user on registration.user_id= user.id where role_id=3 and course.approved ='APPROVED'",
        resultSetMapping = "CourseResult"
)
public class CourseEntity extends BaseEntity {
    @Column (columnDefinition = "TEXT")
    private String title;
    private String image;
    @Column (columnDefinition = "TEXT")
    private String description;
    @Column (columnDefinition = "TEXT")
    private String shortDescription;
    private int price;
    private LocalDateTime dateStart;
    private boolean status;
    private String linkVideoIntro;
    private String approved;
    private LocalDateTime approvedDate;
    private LocalDateTime registerDate;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;

    @OneToMany
    @JoinColumn(name = "course_id")
    private Collection<ThematicEntity> thematics =new ArrayList<>();

}
