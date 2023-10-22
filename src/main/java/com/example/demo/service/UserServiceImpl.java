package com.example.demo.service;
import com.example.demo.Entity.*;
import com.example.demo.dto.ProfileTeacherUpdateRequest;
import com.example.demo.repo.PermissionRepo;
import com.example.demo.repo.ProfileTeacherRepo;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.s3.BucketName;
import com.example.demo.s3.FileStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.demo.security.ApplicationUserRole.ADMIN;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
    private final PasswordEncoder passwordEncoder;
    private  final ConfirmationTokenService confirmationTokenService;
    private final FileStore fileStore;
    private final ProfileTeacherRepo profileTeacherRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username).get();
        if(user == null){
            log.error("User not found in database");
            throw new UsernameNotFoundException("User {} not found in database");
        }else{
            log.info("User found in database: {}",username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            RoleEntity roleEntity =roleRepo.findByName(role.getName());
            roleEntity.getPermission().forEach(permission ->{
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }
    @Override
    public UserEntity saveUser(UserEntity user) {
        log.info("Saving new User {} to database",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Saving new User {} to database", ADMIN.name());
        return userRepo.save(user);
    }

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        log.info("Saving new Role {} to database",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String userName,String role) {
        UserEntity user=userRepo.findByUsername(userName).get();
        RoleEntity roleEntity=roleRepo.findByName(role);
        user.getRoles().add(roleEntity);
        log.info("Adding role {} to user {}",role,userName);
    }

    @Override
    public UserEntity getUser(String userName) {
        log.info("Fetching user {}",userName);
        return userRepo.findByUsername(userName).get();
    }

    @Override
    public List<UserEntity> getUsers() {
        log.info("Fetching all user ");
        return userRepo.findAll();
    }

    @Override
    public void addPermissionToRole(String roleName, String permissonName) {
        log.info("Fetching all user ");
        PermissionEntity permissionEntity=permissionRepo.findByName(permissonName);
        RoleEntity roleEntity=roleRepo.findByName(roleName);
        roleEntity.getPermission().add(permissionEntity);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepo.findById(id).get();
    }

    @Override
    public List<UserEntity> findAllTeacher() {
        return userRepo.findAllByIsTeacher(true);
    }

    @Override
    public byte[] downLoadImg(Long id) {
        return new byte[0];
    }

    @Override
    public UserEntity updateProfileTeacher(Long id, ProfileTeacherUpdateRequest request) {
        UserEntity oldUser = userRepo.findById(id).get();
        UserEntity newUser = new UserEntity();
        ObjectMapper objectMapper = new ObjectMapper();
        ProfileTeacher profile =oldUser.getProfileTeacher();
        if(profile==null){
            profile = new ProfileTeacher();
        }
        try{
            String json = objectMapper.writeValueAsString(oldUser);
            newUser =objectMapper.readValue(json, UserEntity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String nameAvatar;
        String pathAvatar;
        if(request.getAvatarFile()!=null){
            Map<String, String> metadata= getStringStringMap(request.getAvatarFile());
            pathAvatar = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), newUser.getUsername());
            nameAvatar =String.format("%s-%s",request.getAvatarFile().getName()+"avatar",newUser.getUsername());
            newUser.setAvatar(nameAvatar);
            try{
                if(!oldUser.getAvatar().isEmpty())
                fileStore.deleteFile( BucketName.COURSE_IMAGE.getBucketName(),oldUser.getAvatar());
                fileStore.save(pathAvatar,nameAvatar,Optional.of(metadata),request.getAvatarFile().getInputStream());
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String pathImageBg;
        String nameImageBg;
        if(request.getImgBgFile()!=null){
            Map<String, String> metadata= getStringStringMap(request.getImgBgFile());
            pathImageBg = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), newUser.getUsername());
            nameImageBg =String.format("%s-%s",request.getImgBgFile().getName(),newUser.getUsername());
            request.setImageBg(nameImageBg);
            try{
                if(profile.getImageBg()!=null)
                    fileStore.deleteFile( BucketName.COURSE_IMAGE.getBucketName(),profile.getImageBg());
                    fileStore.save(pathImageBg,nameImageBg,Optional.of(metadata),request.getImgBgFile().getInputStream());
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(request.getEmail()!=null){
            newUser.setUsername(request.getEmail());
        }
        if(request.getPassWord()!=null){
            newUser.setPassword(passwordEncoder.encode(request.getPassWord()));
        }
        try{
            String json = objectMapper.writeValueAsString(request);
            profile =objectMapper.readValue(json, ProfileTeacher.class);
            profile.setUser(newUser);
            profileTeacherRepo.save(profile);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return userRepo.save(newUser);
    }


    @Override
    public String signUpUser(UserEntity user) throws IllegalAccessException {
        boolean userExists= userRepo.findByUsername(user.getUsername()).isPresent();
        if(userExists){
            throw new IllegalAccessException("Email already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        String token= UUID.randomUUID().toString();
        ConfirmationToken confirmationToken=new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;

    }

    @Override
    public void addClick(UserClicksOnCourseEntity userClicksOnCourseEntity, UserEntity user) {
        user.getNumOfClick().add(userClicksOnCourseEntity);
    }


    private static Map<String, String> getStringStringMap(MultipartFile file) {
        Map<String, String> metadata= new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));
        return metadata;
    }

}