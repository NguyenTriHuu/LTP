package com.example.demo.service;

import com.example.demo.Entity.FileEntity;
import com.example.demo.Entity.LectureEntity;
import com.example.demo.repo.ConfigCommentRepo;
import com.example.demo.repo.FileRepo;
import com.example.demo.repo.LectureRepo;
import com.example.demo.s3.BucketName;
import com.example.demo.s3.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService{
    private final LectureRepo lectureRepo;
    private final FileStore fileStore;
    private final FileRepo fileRepo;
    private final ConfigCommentRepo configCommentRepo;
    @Override
    public void addFileToLecture(Long idLecture, MultipartFile[] files) {
        LectureEntity lectureEntity = lectureRepo.findById(idLecture).get();
        FileEntity fileEntity = new FileEntity();
        for(MultipartFile file:files){
            Map<String, String> metadata =getStringStringMap(file);
            String path = String.format("%s/%s", BucketName.COURSE_VIDEO.getBucketName(),lectureEntity.getTitle());
            String name = String.format("%s-%s",file.getOriginalFilename(),lectureEntity.getTitle());
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileLink(name);
            fileEntity.setStatus(true);
            try{
                fileStore.save(path,name, Optional.of(metadata),file.getInputStream());
                lectureEntity.getFiles().add(fileRepo.save(fileEntity));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateFileToLecutre(Long idLecture,MultipartFile[] files, Long[] idsUpdateFile, String[] updateNames, Long[] idsUpdateName) {
        LectureEntity lecture =lectureRepo.findById(idLecture).get();
        if(idsUpdateName!=null){
            if(idsUpdateName.length!=0){
                int idx =0;
                for(Long id : idsUpdateName){
                    FileEntity file = fileRepo.findById(id).get();
                    String name = String.format("%s-%s",updateNames[idx],lecture.getTitle());
                    try{
                        fileStore.copyObject(BucketName.COURSE_VIDEO.getBucketName(),file.getFileLink(),name,lecture.getTitle());
                        fileStore.deleteFile(BucketName.COURSE_VIDEO.getBucketName(),file.getFileLink());
                        file.setFileLink(name);
                        file.setFileName(updateNames[idx]);
                        fileRepo.save(file);
                    }catch (Exception e){
                        throw new IllegalStateException(e);
                    }
                    idx++;
                }
            }
        }
        if(idsUpdateFile!=null){
            if(idsUpdateFile.length!=0){
                int idx=0;
                for(Long id :idsUpdateFile){
                    FileEntity file = fileRepo.findById(id).get();
                    Map<String, String> metadata =getStringStringMap(files[idx]);
                    String name = String.format("%s-%s",files[idx].getOriginalFilename(),lecture.getTitle());
                    String path = String.format("%s/%s", BucketName.COURSE_VIDEO.getBucketName(),lecture.getTitle());

                    try{
                        fileStore.deleteFile(BucketName.COURSE_VIDEO.getBucketName(),file.getFileLink());
                        fileStore.save(path,name,Optional.of(metadata),files[idx].getInputStream());
                        file.setFileLink(name);
                        file.setFileName(files[idx].getOriginalFilename());
                        fileRepo.save(file);
                    }catch (Exception e){
                        throw new IllegalStateException(e);
                    }
                    idx++;
                }
            }
        }

    }

    @Override
    public void deleteFileToLecture(Long[] ids) {
        for(Long id : ids){
            fileRepo.deleteFileFromLectureFiles(id);
            fileRepo.deleteFileFromFile(id);
        }
    }

    @Override
    public String getFileUrl(Long idLecture,String link) {
        LectureEntity lectureEntity = lectureRepo.findById(idLecture).get();
        String path =String.format("%s/%s", BucketName.COURSE_VIDEO.getBucketName(),lectureEntity.getTitle());
        String fileName =link;
        return fileStore.getVideoUrl(fileName,path);
    }

    @Override
    public Map<String, String> getListFileUrl(Long idLecture) {
        Map<String, String> data =new HashMap<>();
        LectureEntity lecture = lectureRepo.findById(idLecture).get();
        for(FileEntity file :lecture.getFiles()){
            if(file.getStatus()==true){
                String path =String.format("%s/%s", BucketName.COURSE_VIDEO.getBucketName(),lecture.getTitle());
                String fileName =file.getFileLink();
                String url =fileStore.getVideoUrl(fileName,path);
                data.put(""+file.getFileName(),url);
            }
        }
        return data;
    }

    @Override
    public void updateLecture(Long id, String content, String config) {
        LectureEntity lecture =lectureRepo.findById(id).get();
        lecture.setContent(content);
        lecture.setConfigComment(configCommentRepo.findByConfig(config.toUpperCase()).get());
        lectureRepo.save(lecture);
    }

    @Override
    public FileEntity getFileStart(Long idLecture) {
        LectureEntity lecture =lectureRepo.findById(idLecture).get();
        FileEntity file =lecture.getFiles().iterator().next();
        return file;
    }

    private static Map<String, String> getStringStringMap(MultipartFile file) {
        Map<String, String> metadata= new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));
        return metadata;
    }
}
