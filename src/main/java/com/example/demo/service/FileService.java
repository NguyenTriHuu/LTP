package com.example.demo.service;

import com.example.demo.Entity.FileEntity;
import com.example.demo.Entity.LectureEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {
    void addFileToLecture(Long idLecture, MultipartFile[] files);

    void updateFileToLecutre(Long idLecture,MultipartFile[] files,Long[] idsUpdateFile,String[] updateNames,Long[] idsUpdateName);

    void deleteFileToLecture(Long[] ids);

    String getFileUrl(Long idLecture,String link);

    Map<String,String> getListFileUrl(Long idLecture);

    void updateLecture(Long id, String content, String config);

    FileEntity getFileStart (Long idLecture);
}
