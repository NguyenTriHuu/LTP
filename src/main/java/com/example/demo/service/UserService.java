package com.example.demo.service;

import com.example.demo.Entity.RoleEntity;
import com.example.demo.Entity.UserClicksOnCourseEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.ProfileTeacherUpdateRequest;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    RoleEntity saveRole(RoleEntity role);
    void addRoleToUser(String userName, String role);
    UserEntity getUser(String userName);
    List<UserEntity> getUsers();
     String signUpUser(UserEntity user) throws IllegalAccessException;

     void addClick(UserClicksOnCourseEntity userClicksOnCourseEntity,UserEntity user);

    void addPermissionToRole(String roleName,String permissonName);

    UserEntity getUserById(Long id);

    List<UserEntity> findAllTeacher();

    byte[] downLoadImg(Long id);
    byte[] downLoadAvatar(Long id);

    UserEntity updateProfileTeacher(Long id, ProfileTeacherUpdateRequest request);

    UserEntity getTeacherByCourse(Long idCourse);

    void setVerifiUser(String userName);
}
