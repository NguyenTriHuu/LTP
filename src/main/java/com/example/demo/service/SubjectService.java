package com.example.demo.service;

import com.example.demo.Entity.SubjectEntity;
import com.example.demo.dto.ProgramRequest;
import com.example.demo.dto.SubjectReqRes;

import java.util.List;

public interface SubjectService {
    void save(SubjectEntity subject);

    List<SubjectReqRes> getAll();

    List<SubjectReqRes> getByProgram(Long idProgram);

    SubjectReqRes save (SubjectReqRes subjectReqRes);

    SubjectReqRes update (SubjectReqRes subjectReqRes);
}
