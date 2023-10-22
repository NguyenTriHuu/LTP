package com.example.demo.service;

import com.example.demo.Entity.SubjectEntity;
import com.example.demo.repo.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService{
    private final SubjectRepo subjectRepo;
    @Override
    public void save(SubjectEntity subject) {
        subjectRepo.save(subject);
    }
}
