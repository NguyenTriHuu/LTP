package com.example.demo.service;

import com.example.demo.Entity.RoleEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.GetUserReqRes;
import com.example.demo.dto.ProfileTeacherUpdateRequest;
import com.example.demo.dto.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    RoleEntity saveRole(RoleEntity role);
    void addRoleToUser(String userName, String role);
    UserEntity getUser(String userName);
    List<UserEntity> getUsers();
     String signUpUser(UserEntity user) throws IllegalAccessException;

    void addPermissionToRole(String roleName,String permissonName);

    UserEntity getUserById(Long id);

    List<UserEntity> findAllTeacher();

    byte[] downLoadImg(Long id);
    byte[] downLoadAvatar(Long id);

    UserEntity updateProfileTeacher(Long id, ProfileTeacherUpdateRequest request);

    UserEntity getTeacherByCourse(Long idCourse);

    void setVerifiUser(String userName);

    List<GetUserReqRes> findUsersByFilter(GetUserReqRes getUserReqRes);

    UserEntity changeRole(Long id, List<String> roles);

    UserEntity changeStatus(Long id, Boolean status);

    void testDle (Long id);

    UserProfileResponse getUserProfile(String userName);

    void updateFullName(String userName, String fullName);
    void updateUserName(String userName, String userNameUpdate);

    void updatePassword(String userName, String password);

    byte[] updateAvatar(String userName, MultipartFile newAvatar);
}
