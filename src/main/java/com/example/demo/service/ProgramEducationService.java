package com.example.demo.service;

import com.example.demo.Entity.EducationProgramEntity;

public interface ProgramEducationService {
    void save (EducationProgramEntity educationProgram);
    void addSubjectToProgram(String program, String subject);
}
