package com.example.demo.repo;

import com.example.demo.Entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepo extends JpaRepository<FileEntity,Long> {
    @Modifying
    @Query(value ="DELETE FROM lecture_files WHERE files_id = :idFile", nativeQuery = true)
    void deleteFileFromLectureFiles(@Param("idFile") Long idFile);

    @Modifying
    @Query(value ="DELETE FROM file WHERE id = :idFile", nativeQuery = true)
    void deleteFileFromFile(@Param("idFile") Long idFile);
}
