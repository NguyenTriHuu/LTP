package com.example.demo.service;

import com.example.demo.Entity.LectureEntity;
import com.example.demo.Entity.ThematicEntity;
import com.example.demo.repo.LectureRepo;
import com.example.demo.repo.ThematicRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ThematicServiceImpl implements ThematicService{
    private final ThematicRepo thematicRepo;
    private final LectureRepo lectureRepo;
    @Override
    public void addLectureToThematic(long idThematic, long idLecture) {
        ThematicEntity thematic = thematicRepo.findById(idThematic).get();
        LectureEntity lecture =lectureRepo.findById(idLecture).get();
        thematic.getLectures().add(lecture);
    }
}
