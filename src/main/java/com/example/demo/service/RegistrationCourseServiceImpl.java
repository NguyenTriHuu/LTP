package com.example.demo.service;

import com.example.demo.Entity.RegistrationsEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.repo.CourseRepo;
import com.example.demo.repo.RegistrationRepo;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegistrationCourseServiceImpl implements RegistrationCourseService{
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final RegistrationRepo registrationRepo;
    private final RoleRepo roleRepo;
    @Override
    public Boolean checkIsStudent(String userName, Long idCourse) {
        UserEntity user = userRepo.findByUsername(userName).get();
        Boolean is = registrationRepo.findByUserAndCourse(idCourse,user.getId(),roleRepo.findByName("STUDENT").getId()).isPresent();
        return is;
    }
}
