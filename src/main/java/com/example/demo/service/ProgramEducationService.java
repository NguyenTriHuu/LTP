package com.example.demo.service;

import com.example.demo.Entity.EducationProgramEntity;
import com.example.demo.dto.ProgramRequest;

import java.util.List;

public interface ProgramEducationService {
    void save (EducationProgramEntity educationProgram);
    void addSubjectToProgram(String program, String subject);

    List<ProgramRequest> getAll();
    List<EducationProgramEntity> getByCategory(Long idCategory);

    EducationProgramEntity save (ProgramRequest programRequest);
    EducationProgramEntity update (ProgramRequest programRequest);
}
