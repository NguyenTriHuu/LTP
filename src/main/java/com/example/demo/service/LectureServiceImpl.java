package com.example.demo.service;

import com.example.demo.Entity.LectureEntity;
import com.example.demo.repo.LectureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureServiceImpl implements LectureService{
    private final LectureRepo lectureRepo;
    @Override
    public LectureEntity getLecture(Long id) {
        return lectureRepo.findById(id).get();
    }
}
