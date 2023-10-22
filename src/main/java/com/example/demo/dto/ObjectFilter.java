package com.example.demo.dto;

import lombok.Data;

@Data
public class ObjectFilter {
    private Boolean status;
    private String subject;
    private String category;
    private String program;
    private int offSet;
    private String stringQuery;
    private int rowsPerPage;
    private String sort;
}
