package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserReqRes {
    private Long id;
    private String userName;
    private String fullname;
    private LocalDateTime dateOfBirth;
    private Boolean locked;
    private String role;
    private int rowsPerPage;
    private int page;
}
