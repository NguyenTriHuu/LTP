package com.example.demo.repo;

import com.example.demo.Entity.ProfileTeacher;
import com.example.demo.dto.ProfileTeacherUpdateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileTeacherRepo extends JpaRepository<ProfileTeacher,Long> {

}
