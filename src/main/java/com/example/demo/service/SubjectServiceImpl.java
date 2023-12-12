package com.example.demo.service;

import com.example.demo.Entity.EducationProgramEntity;
import com.example.demo.Entity.SubjectEntity;
import com.example.demo.dto.SubjectReqRes;
import com.example.demo.repo.ProgramEducationRepo;
import com.example.demo.repo.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService{
    private final SubjectRepo subjectRepo;
    private final ProgramEducationRepo programEducationRepo;
    @Override
    public void save(SubjectEntity subject) {
        subjectRepo.save(subject);
    }

    @Override
    public List<SubjectReqRes> getAll() {
        List<SubjectEntity> listEntity = subjectRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<SubjectReqRes> listRes = new ArrayList<>();
        for(SubjectEntity subject: listEntity){
            SubjectReqRes subjectReqRes = new SubjectReqRes();
            subjectReqRes.setId(subject.getId());
            subjectReqRes.setName(subject.getName());
            subjectReqRes.setCode(subject.getCode());
            Long idProgram = subjectRepo.getProgramBySubject(subject.getId());
            subjectReqRes.setIdProgram(idProgram);
            listRes.add(subjectReqRes);
        }
        return listRes;
    }

    @Override
    public List<SubjectReqRes> getByProgram(Long idProgram) {
        List<SubjectEntity> listSubject = subjectRepo.getAllByProgram(idProgram);
        List<SubjectReqRes> listRes = new ArrayList<>();
        for(SubjectEntity subjectEntity: listSubject){
            SubjectReqRes subjectReqRes = new SubjectReqRes();
            subjectReqRes.setId(subjectEntity.getId());
            subjectReqRes.setName(subjectEntity.getName());
            subjectReqRes.setCode(subjectEntity.getCode());
            subjectReqRes.setIdProgram(idProgram);
            listRes.add(subjectReqRes);
        }
        return listRes;
    }

    @Override
    public SubjectReqRes save(SubjectReqRes subjectReqRes) {
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName(subjectReqRes.getName());
        subjectEntity.setCode(subjectReqRes.getCode());
        SubjectEntity subjectSave = subjectRepo.save(subjectEntity);
        EducationProgramEntity program = programEducationRepo.findById(subjectReqRes.getIdProgram()).get();
        program.getSubjects().add(subjectSave);
        subjectReqRes.setId(subjectSave.getId());
        return subjectReqRes;
    }

    @Override
    public SubjectReqRes update(SubjectReqRes subjectReqRes) {
        SubjectEntity subjectEntity = subjectRepo.findById(subjectReqRes.getId()).get();
        subjectEntity.setName(subjectReqRes.getName());
        subjectEntity.setCode(subjectReqRes.getCode());
        subjectRepo.save(subjectEntity);
        return subjectReqRes;
    }
}
