package com.example.demo.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name ="user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    private String username;
    private String password;
    private String fullname;
    private LocalDateTime dateOfBirth;
    private Boolean locked;
    private String avatar;
    private LocalDateTime active;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<RoleEntity> roles= new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private Collection<AddressEntity> address = new ArrayList<>();


    @OneToMany
    @JoinColumn(name = "user_id")
    private Collection<ProfileTeacher> profileTeacher = new ArrayList<>();

    public UserEntity(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;

    }

    public UserEntity( String username, String password, String fullname, Boolean locked, Boolean isTeacher, Boolean isManager, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.locked = locked;
    }
}
