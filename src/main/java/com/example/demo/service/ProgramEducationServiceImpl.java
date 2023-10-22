package com.example.demo.service;

import com.example.demo.Entity.EducationProgramEntity;
import com.example.demo.Entity.SubjectEntity;
import com.example.demo.repo.ProgramEducationRepo;
import com.example.demo.repo.SubjectRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProgramEducationServiceImpl implements  ProgramEducationService{
    private final ProgramEducationRepo programEducationRepo;
    private final SubjectRepo subjectRepo;
    @Override
    public void save(EducationProgramEntity educationProgram) {
            programEducationRepo.save(educationProgram);
    }

    @Override
    public void addSubjectToProgram(String program, String subject) {
        EducationProgramEntity educationProgram = programEducationRepo.findByName(program).get();
        SubjectEntity subjectEntity = subjectRepo.findByName(subject).get();
        educationProgram.getSubjects().add(subjectEntity);
        log.info("Adding Subject {} to Program {}",subject,program);
    }
}
