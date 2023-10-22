package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name ="profile_teacher")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teachingSubject;
    private String degree;
    private String workplace;
    private String linkFb;
    private String imageBg;
    private String email;
    private String contact;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
