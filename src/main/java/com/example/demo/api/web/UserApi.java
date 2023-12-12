package com.example.demo.api.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.CourseUpdateRequest;
import com.example.demo.dto.ProfileTeacherUpdateRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class UserApi {
    private final UserService userService;

    @GetMapping("/user/allteacher")
    public ResponseEntity<List<UserEntity>> findAllTeacher(){
        return ResponseEntity.ok().body(userService.findAllTeacher());
    }

    @GetMapping("/user/image/{id}/download")
    public byte[]  downloadthumdnail (@PathVariable("id") Long id){

        return userService.downLoadImg(id);
    }

    @PutMapping(value = "/user/update/profile/teacher/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //  @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserEntity> updateProfileTeacher(@PathVariable("id") Long id,
                                               @ModelAttribute ProfileTeacherUpdateRequest profile){

        return ResponseEntity.ok().body(userService.updateProfileTeacher(id,profile));
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserEntity> findUser(@RequestHeader(AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm= Algorithm.HMAC256("secrect".getBytes());
        JWTVerifier verifier= JWT.require(algorithm).build();
        DecodedJWT decodedJWT= verifier.verify(token);
        String username=decodedJWT.getSubject();
        return ResponseEntity.ok().body(userService.getUser(username));
    }

    @GetMapping(value = "/user/teacher/course/{id}")
    public ResponseEntity<UserEntity> findTeacherByCourse(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(userService.getTeacherByCourse(id));
    }

    @GetMapping(value = "/user/teacher/course/{id}/avatar")
    public byte[] GetAvatarTeacherByCourse(@PathVariable("id") Long id){
        //UserEntity user =userService.getTeacherByCourse(id);
        return userService.downLoadAvatar(id);
    }

    @PostMapping(value = "/user/{id}/change-status")
    public ResponseEntity<UserEntity> changeStatus(@PathVariable("id") Long id,@RequestBody Boolean lock){
            return ResponseEntity.ok().body(userService.changeStatus(id,lock));
    }

    @PostMapping(value = "/user/{id}/change-role")
    public ResponseEntity<UserEntity> changeRole(@PathVariable("id") Long id,@RequestBody List<String> roleName){
        return ResponseEntity.ok().body(userService.changeRole(id,roleName));
    }

    @PostMapping(value = "/user/{id}/delte-test")
    public void testdelete(@PathVariable("id") Long id){
                userService.testDle(id);
    }

    @GetMapping(value = "/user/get/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestParam String userName){
        return ResponseEntity.ok().body(userService.getUserProfile(userName));
    }

    @PostMapping(value = "/user/{username}/profile/update-fullname")
    public void updateFullName(@PathVariable("username") String username , @RequestBody UserProfileResponse userProfileResponse){
        userService.updateFullName(username, userProfileResponse.getFullName());
    }

    @PostMapping(value = "/user/{username}/profile/update-username")
    public void updateUserName(@PathVariable("username") String username , @RequestBody UserProfileResponse userProfileResponse){
        userService.updateUserName(username,userProfileResponse.getUserName());
    }

    @PostMapping(value = "/user/{username}/profile/update-password")
    public void updatePassword(@PathVariable("username") String username , @RequestBody UserProfileResponse userProfileResponse){
        userService.updatePassword(username,userProfileResponse.getPassword());
    }

    @PostMapping(value = "/user/{username}/profile/update-avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public byte[] updateAvatar(@PathVariable("username") String username , @ModelAttribute UserProfileResponse userProfileResponse){
        return userService.updateAvatar(username,userProfileResponse.getNewAvatar());
    }

    @GetMapping(value = "/user/{userName}/avatar")
    public byte[] GetAvatar(@PathVariable("userName") String userName){
        UserEntity user = userService.getUser(userName);
        return userService.downLoadAvatar(user.getId());
    }
}
