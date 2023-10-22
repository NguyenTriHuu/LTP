package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProgramEducationResponse {
    private String nameProgramEducationResponse;
    private String codeProgram;
    private List<SubjectReponse> subjectResponseList;
}
