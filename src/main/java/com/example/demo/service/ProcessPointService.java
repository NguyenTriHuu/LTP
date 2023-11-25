package com.example.demo.service;

import com.example.demo.Entity.ProcessPointEntity;
import com.example.demo.dto.ProcessResponse;

public interface ProcessPointService {
    void save(Long idCourse, Long idUser);
    ProcessResponse next(Long idCourse, String userName);

    ProcessPointEntity getProcessPointByCourseAndUser(Long idCourse, Long idUser);

    ProcessResponse getPointCurrent (Long idCourse, String userName);

}
