package com.example.demo.service;
import com.amazonaws.util.IOUtils;
import com.example.demo.Entity.*;
import com.example.demo.dto.GetUserReqRes;
import com.example.demo.dto.ProfileTeacherUpdateRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.repo.ProfileTeacherRepo;
import com.example.demo.repo.RegistrationRepo;
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
    private final PasswordEncoder passwordEncoder;
    private  final ConfirmationTokenService confirmationTokenService;
    private final FileStore fileStore;
    private final ProfileTeacherRepo profileTeacherRepo;
    private final RegistrationRepo registrationRepo;
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
       /* log.info("Fetching all user ");
        PermissionEntity permissionEntity=permissionRepo.findByName(permissonName);
        RoleEntity roleEntity=roleRepo.findByName(roleName);
        roleEntity.getPermission().add(permissionEntity);*/
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepo.findById(id).get();
    }

    @Override
    public List<UserEntity> findAllTeacher() {
        return userRepo.findAllByIsTeacher();
    }

    @Override
    public byte[] downLoadImg(Long id) {
        UserEntity user = userRepo.findById(id).get();
        String path = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), user.getUsername());
        String key = user.getProfileTeacher().iterator().next().getImageBg();
        try {
            return  IOUtils.toByteArray(fileStore.getObject(path,key).getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downLoadAvatar(Long id) {
        UserEntity user = userRepo.findById(id).get();
        String path = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), user.getUsername());
        String key =user.getAvatar();
        if(key==null){
            return null;
        }
        try {
            return  IOUtils.toByteArray(fileStore.getObject(path,key).getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity updateProfileTeacher(Long id, ProfileTeacherUpdateRequest request) {
        UserEntity oldUser = userRepo.findById(id).get();
        UserEntity newUser = new UserEntity();
        ObjectMapper objectMapper = new ObjectMapper();
        ProfileTeacher profile = new ProfileTeacher();
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
                if(oldUser.getAvatar()!=null)
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
                if(oldUser.getProfileTeacher().size()==0){
                    fileStore.save(pathImageBg,nameImageBg,Optional.of(metadata),request.getImgBgFile().getInputStream());
                }else{
                    if(profile.getImageBg()!=null)
                        fileStore.deleteFile( BucketName.COURSE_IMAGE.getBucketName(),profile.getImageBg());
                        fileStore.save(pathImageBg,nameImageBg,Optional.of(metadata),request.getImgBgFile().getInputStream());
                }

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
        if(request.getFullName()!=null){
            newUser.setFullname(request.getFullName());
        }
        try{
            String json = objectMapper.writeValueAsString(request);
            profile =objectMapper.readValue(json, ProfileTeacher.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(oldUser.getProfileTeacher().size()==0){
            //Add profile
            ProfileTeacher newProfile =profileTeacherRepo.save(profile);
            UserEntity user =userRepo.save(newUser);
            user.getProfileTeacher().add(newProfile);
        }else{
            //Update profile
            ProfileTeacher oldProfile = userRepo.save(newUser).getProfileTeacher().iterator().next();
            try{
                String json = objectMapper.writeValueAsString(oldProfile);
                profile =objectMapper.readValue(json, ProfileTeacher.class);
                profileTeacherRepo.save(profile);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }

        return newUser;
    }

    @Override
    public UserEntity getTeacherByCourse(Long idCourse) {
        Long roleId = roleRepo.findByName("TEACHER").getId();
        RegistrationsEntity registrationsEntity = registrationRepo.findByCourseAndRole(idCourse,roleId).get();
        return registrationsEntity.getUser();
    }

    @Override
    public void setVerifiUser(String userName) {
        UserEntity user =userRepo.findByUsername(userName).get();
        user.setLocked(false);
        userRepo.save(user);
    }

    @Override
    public List<GetUserReqRes> findUsersByFilter(GetUserReqRes getUserReqRes) {
        return null;
    }

    @Override
    public UserEntity changeRole(Long id, List<String> roles) {
        UserEntity user = userRepo.findById(id).get();
        userRepo.deleteRoleOfUser(user.getId());
        Collection<RoleEntity> rolesEntity = new ArrayList<>();
        for(String roleName: roles){
            RoleEntity role = roleRepo.findByName(roleName);
            rolesEntity.add(role);
        }
        user.setRoles(rolesEntity);
        return userRepo.save(user);
    }

    @Override
    public UserEntity changeStatus(Long id, Boolean status) {
        UserEntity user = userRepo.findById(id).get();
        user.setLocked(status);
        return userRepo.save(user);
    }

    @Override
    public void testDle(Long id) {
        userRepo.deleteRoleOfUser(id);
    }

    @Override
    public UserProfileResponse getUserProfile(String userName) {
        UserEntity user = userRepo.findByUsername(userName).get();
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setUserName(user.getUsername());
        userProfileResponse.setFullName(user.getFullname());
        userProfileResponse.setAvatar(user.getAvatar());
        return userProfileResponse;
    }

    @Override
    public void updateFullName(String userName, String fullName) {
        UserEntity userEntity = userRepo.findByUsername(userName).get();
        userEntity.setFullname(fullName);
        userRepo.save(userEntity);
    }

    @Override
    public void updateUserName(String userName, String userNameUpdate) {
        Boolean exist = userRepo.findByUsername(userName).isPresent();
        if(exist){
            UserEntity userEntity = userRepo.findByUsername(userName).get();
            userEntity.setUsername(userNameUpdate);
            userRepo.save(userEntity);
        }
    }

    @Override
    public void updatePassword(String userName, String password) {
        UserEntity user = userRepo.findByUsername(userName).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);

    }

    @Override
    public byte[] updateAvatar(String userName, MultipartFile newAvatar) {
        UserEntity userEntity = userRepo.findByUsername(userName).get();
        String nameAvatar;
        String pathAvatar;
        if(newAvatar!=null){
            Map<String, String> metadata= getStringStringMap(newAvatar);
            pathAvatar = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), userEntity.getUsername());
            nameAvatar =String.format("%s-%s",newAvatar.getName()+"avatar",userEntity.getUsername());
            try{
                if(userEntity.getAvatar()!=null)
                    fileStore.deleteFile( BucketName.COURSE_IMAGE.getBucketName(),userEntity.getAvatar());
                    fileStore.save(pathAvatar,nameAvatar,Optional.of(metadata),newAvatar.getInputStream());
                    userEntity.setAvatar(nameAvatar);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        UserEntity userEntity1= userRepo.save(userEntity);
        String path = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(),userEntity1.getUsername());
        String key =userEntity1.getAvatar();
        try {
            return  IOUtils.toByteArray(fileStore.getObject(path,key).getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String signUpUser(UserEntity user) throws IllegalAccessException {
        boolean userExists= userRepo.findByUsername(user.getUsername()).isPresent();
        if(userExists){
            throw new IllegalAccessException("Email already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setLocked(false);
        List<RoleEntity> list = new ArrayList<>();
        RoleEntity role = roleRepo.findByName("STUDENT");
        list.add(role);
        user.setRoles(list);
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




    private static Map<String, String> getStringStringMap(MultipartFile file) {
        Map<String, String> metadata= new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));
        return metadata;
    }



}
