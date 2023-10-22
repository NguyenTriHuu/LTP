package com.example.demo.service;

import com.example.demo.Entity.UserClicksOnCourseEntity;
import com.example.demo.repo.UserClickOnCourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserClickOnCourseServiceImpl implements UserClickOnCourseService{
    private final UserClickOnCourseRepo userClickOnCourseRepo;
    @Override
    public void save(UserClicksOnCourseEntity userClicksOnCourseEntity) {
        userClickOnCourseRepo.save(userClicksOnCourseEntity);
    }
}
