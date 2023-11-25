package com.example.demo.service;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.EducationProgramEntity;
import com.example.demo.Entity.SubjectEntity;
import com.example.demo.dto.ProgramRequest;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ProgramEducationRepo;
import com.example.demo.repo.SubjectRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProgramEducationServiceImpl implements  ProgramEducationService{
    private final ProgramEducationRepo programEducationRepo;
    private final SubjectRepo subjectRepo;
    private final CategoryRepo categoryRepo;
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

    @Override
    public List<EducationProgramEntity> getAll() {
        return programEducationRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @Override
    public List<EducationProgramEntity> getByCategory(Long idCategory) {
        return programEducationRepo.findByCategory(idCategory);
    }

    @Override
    public EducationProgramEntity save(ProgramRequest programRequest) {
        EducationProgramEntity educationProgram = new EducationProgramEntity();
        educationProgram.setCode(programRequest.getCode());
        educationProgram.setName(programRequest.getName());
        EducationProgramEntity educationProgram2 =programEducationRepo.save(educationProgram);
        CategoryEntity category = categoryRepo.findById(programRequest.getIdCategory()).get();
        category.getPrograms().add(educationProgram2);
        return educationProgram2;
    }

    @Override
    public EducationProgramEntity update(ProgramRequest programRequest) {
        EducationProgramEntity programEntity = programEducationRepo.findById(programRequest.getId()).get();
        programEntity.setName(programRequest.getName());
        programEntity.setCode(programRequest.getCode());
        return programEducationRepo.save(programEntity);
    }
}
