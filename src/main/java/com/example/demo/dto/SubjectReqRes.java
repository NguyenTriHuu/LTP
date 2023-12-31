package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectReqRes {
    private Long id;
    private String name;
    private String code;
    private Long idProgram;
}
