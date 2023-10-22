package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.LectureEntity;
import com.example.demo.Entity.ThematicEntity;
import com.example.demo.dto.*;
import com.example.demo.repo.*;
import com.example.demo.s3.BucketName;
import com.example.demo.s3.FileStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService{
    private final CourseRepo courseRepo;
    private final FileStore fileStore;
    private final UserRepo userRepo;
    private final SubjectRepo subjectRepo;
    private final ThematicRepo thematicRepo;
    private final LectureRepo lectureRepo;

    @Override
    public CourseEntity findCourseById(Long id) {
        return courseRepo.findById(id).get();
    }

    @Override
    public CourseEntity save(CourseSaveRequest course) {
        CourseEntity courseEntity =new CourseEntity();
        if(course.getFile().isEmpty()){
            throw new IllegalStateException("Cannot upload empty file ["+ course.getFile().getSize()+"]");
        }
        if(course.getThumdnail().isEmpty()){
            throw new IllegalStateException("Cannot upload empty file ["+ course.getFile().getSize()+"]");
        }

        Map<String, String> metadataFile = getStringStringMap(course.getFile());
        Map<String, String> metadataThumdnail = getStringStringMap(course.getThumdnail());
        String pathThumdnail= String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), course.getTitle());
        String thumdnailName =String.format("%s-%s",course.getThumdnail().getName(),course.getTitle());
        String pathFile = String.format("%s/%s", BucketName.COURSE_VIDEO.getBucketName(), course.getTitle());
        String fileName =String.format("%s-%s",course.getFile().getName(),course.getTitle());
        courseEntity.setShortDescription(course.getShortDescription());
        courseEntity.setImage(thumdnailName);
        courseEntity.setPrice(course.getPrice());
        courseEntity.setTeacher(userRepo.findById(course.getTeacherId()).get());
        courseEntity.setTitle(course.getTitle());
        courseEntity.setSubject(subjectRepo.findByCode(course.getSubject()).get());
        courseEntity.setDescription(course.getDescription());
        courseEntity.setDateStart(course.getDateTime());
        courseEntity.setLinkVideoIntro(fileName);
        try{

            fileStore.save(pathFile,fileName,Optional.of(metadataFile),course.getFile().getInputStream());
            fileStore.save(pathThumdnail,thumdnailName,Optional.of(metadataThumdnail),course.getThumdnail().getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return courseRepo.save(courseEntity);
    }

    private static Map<String, String> getStringStringMap(MultipartFile file) {
        Map<String, String> metadata= new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));
        return metadata;
    }

    @Override
    public void saveCourseDetail(Long id,CourseDetailRequest courseDetailRequest) {
        CourseEntity courseEntity = courseRepo.findById(id).get();
        List<ThematicEntity> thematics = getThematicEntities(courseDetailRequest.getThematics());
        Iterable<ThematicEntity> itrThem=thematics;
        thematicRepo.saveAll(itrThem);
        courseEntity.setThematics(thematics);
        courseRepo.save(courseEntity);
    }

    private List<ThematicEntity> getThematicEntities(List<ThematicRequest> thematicRequests) {
        List<ThematicEntity> thematics =new ArrayList<>();
        for(ThematicRequest thematic: thematicRequests){
            ThematicEntity thematicEntity = new ThematicEntity();
            thematicEntity.setName(thematic.getName());
            List<LectureEntity> lectures = new ArrayList<>();
            for(LectureRequest lecture:thematic.getLectures()){
                LectureEntity lectureEntity= new LectureEntity();
                lectureEntity.setTitle(lecture.getTitle());
                lectureEntity.setLocked(true);
                lectures.add(lectureEntity);
            }
            thematicEntity.setLectures(lectures);
            thematics.add(thematicEntity);
            Iterable<LectureEntity> itrLect= lectures;
            lectureRepo.saveAll(itrLect);
        }
        return thematics;
    }

    @Override
    public ListCourseResponse findAllByFilter(Map<String, Object> params) {
        ListCourseResponse listCourseResponse =new ListCourseResponse();
        ObjectFilter objectFilter =new ObjectFilter();
        if(params.containsKey("status")) objectFilter.setStatus(Boolean.valueOf((String)params.get("status")));
        else objectFilter.setStatus(false);
        if(params.containsKey("stringQuery")) objectFilter.setStringQuery((String)params.get("stringQuery"));
        else objectFilter.setStringQuery("");
        if(params.containsKey("page")){
            if(params.containsKey("rowsPerPage"))
                objectFilter.setRowsPerPage(Integer.parseInt((String)params.get("rowsPerPage")));
            else
                objectFilter.setRowsPerPage(10);
            objectFilter.setOffSet((Integer.parseInt((String)params.get("page"))-1)*objectFilter.getRowsPerPage());
        }else{
            objectFilter.setOffSet(0);
            objectFilter.setRowsPerPage(50);
        }
        if(params.containsKey("sort")) objectFilter.setSort((String)params.get("sort"));
        else objectFilter.setSort("desc");
        if(params.containsKey("subject")) objectFilter.setSubject((String)params.get("subject"));
        else objectFilter.setSubject("");
        if(params.containsKey("program")) objectFilter.setProgram((String)params.get("program"));
        else objectFilter.setProgram("");
        if(params.containsKey("category")) objectFilter.setCategory((String)params.get("category"));
        else objectFilter.setCategory("");

        int totalItem =courseRepo.findAllByFilter("%"+
                        objectFilter.getCategory()+"%",
                "%"+objectFilter.getProgram()+"%",
                "%"+objectFilter.getSubject()+"%",
                "%"+objectFilter.getStringQuery()+"%",
                objectFilter.getStatus(),
                0,
                100).size();

        listCourseResponse.setListCourse(courseRepo.findAllByFilter("%"+
                        objectFilter.getCategory()+"%",
                "%"+objectFilter.getProgram()+"%",
                "%"+objectFilter.getSubject()+"%",
                "%"+objectFilter.getStringQuery()+"%",
                objectFilter.getStatus(),
                objectFilter.getOffSet(),
                objectFilter.getRowsPerPage()
        ));
        if((Integer.parseInt((String)params.get("page")))*objectFilter.getRowsPerPage()<=totalItem)
        listCourseResponse.setNextPage(Integer.parseInt((String)params.get("page"))+1);
        listCourseResponse.setPreviousPage(Integer.parseInt((String)params.get("page")));
        return listCourseResponse;

    }

    @Override
    public CourseEntity update(CourseUpdateRequest courseUpdateRequest) throws JsonProcessingException {
        if(courseRepo.findById(courseUpdateRequest.getId()).isEmpty()){
            throw new IllegalStateException("Course not founded in database");
        }
        // Update New Course
        CourseEntity oldCourse = courseRepo.findById(courseUpdateRequest.getId()).get();
        CourseEntity newCourse =new CourseEntity();
        extracted(oldCourse, newCourse);
        // update property new Entity
        if(courseUpdateRequest.getTitle()!=null){
            newCourse.setTitle(courseUpdateRequest.getTitle());
        }
        if(courseUpdateRequest.getDescription()!=null){
            newCourse.setDescription(courseUpdateRequest.getDescription());
        }
        if(courseUpdateRequest.getPrice()!=null){
            newCourse.setPrice(courseUpdateRequest.getPrice());
        }
        if(courseUpdateRequest.getDateTime()!=null){
            newCourse.setDateStart(courseUpdateRequest.getDateTime());
        }
        if(courseUpdateRequest.getShortDescription()!= null){
            newCourse.setShortDescription(courseUpdateRequest.getShortDescription());
        }
        String pathFile;
        String fileName;
        String pathThumdnail;
        String thumdnailName;
        if(courseUpdateRequest.getFile()!=null){
            Map<String, String> metadata= getStringStringMap(courseUpdateRequest.getFile());
             pathFile = String.format("%s/%s", BucketName.COURSE_VIDEO.getBucketName(), courseUpdateRequest.getTitle());
             fileName =String.format("%s-%s",courseUpdateRequest.getFile().getName(),courseUpdateRequest.getTitle());
             newCourse.setImage(fileName);
             try{
                 fileStore.deleteFile( BucketName.COURSE_VIDEO.getBucketName(),oldCourse.getLinkVideoIntro());
                 fileStore.save(pathFile,fileName,Optional.of(metadata),courseUpdateRequest.getFile().getInputStream());
             }catch (IOException e) {
                 throw new RuntimeException(e);
             }
        }
        if(courseUpdateRequest.getThumnail()!=null){
            Map<String, String> metadata= getStringStringMap(courseUpdateRequest.getThumnail());
            pathThumdnail = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), courseUpdateRequest.getTitle());
            thumdnailName =String.format("%s-%s",courseUpdateRequest.getThumnail().getName(),courseUpdateRequest.getTitle());
            newCourse.setImage(thumdnailName);
            try{
                fileStore.deleteFile( BucketName.COURSE_VIDEO.getBucketName(),oldCourse.getLinkVideoIntro());
                fileStore.save(pathThumdnail,thumdnailName,Optional.of(metadata),courseUpdateRequest.getThumnail().getInputStream());
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(courseUpdateRequest.getTeacherId()!=null){
            newCourse.setTeacher(userRepo.findById(courseUpdateRequest.getTeacherId()).get());
        }
        if(courseUpdateRequest.getSubject()!=null){
            newCourse.setSubject(subjectRepo.findByCode(courseUpdateRequest.getSubject()).get());
        }
        if(courseUpdateRequest.getDuration()!=null){
            newCourse.setDuration(courseUpdateRequest.getDuration());
        }
        if(courseUpdateRequest.getStatus()!=null){
            newCourse.setStatus(courseUpdateRequest.getStatus());
        }
        if(courseUpdateRequest.getThematics()!=null){
            ObjectMapper objectMapper = new ObjectMapper();
            List<ThematicEntity> thematics = objectMapper.readValue(courseUpdateRequest.getThematics(), new TypeReference<List<ThematicEntity>>() {});
            for(ThematicEntity thematic: thematics){
                Iterable<LectureEntity> itrLec=thematic.getLectures();
                lectureRepo.saveAll(itrLec);
            }
            Iterable<ThematicEntity> itrThem=thematics;
            thematicRepo.saveAll(itrThem);
            newCourse.setThematics(new ArrayList<>());
            newCourse.setThematics(thematics);
        }
        try{
            courseRepo.save(newCourse);
        }catch (Exception e){
            throw new IllegalStateException("Faile to update : "+e);
        }
        return courseRepo.save(newCourse);
    }

    @Override
    public byte[] downloadThumdnail(Long id) {
        CourseEntity course= courseRepo.findById(id).get();
        String path=String.format("%s/%s",BucketName.COURSE_IMAGE.getBucketName(),course.getTitle());
        String key =course.getImage();
        try {
            return  IOUtils.toByteArray(fileStore.getObject(path,key).getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public S3Object downloadVideo(Long id) {
        CourseEntity course= courseRepo.findById(id).get();
        String path=String.format("%s/%s",BucketName.COURSE_VIDEO.getBucketName(),course.getTitle());
        String key =course.getLinkVideoIntro();
        return fileStore.getObject(path,key);
    }

    @Override
    public String getVideoUrl(Long id) {
        CourseEntity course = courseRepo.findById(id).get();
        String path =String.format("%s/%s", BucketName.COURSE_VIDEO.getBucketName(), course.getTitle());
        String fileName =course.getLinkVideoIntro();
        return fileStore.getVideoUrl(fileName,path);
    }

    private static void extracted(CourseEntity oldCourse, CourseEntity newCourse) {
        newCourse.setId(oldCourse.getId());
        newCourse.setImage(oldCourse.getImage());
        newCourse.setPrice(oldCourse.getPrice());
        newCourse.setTeacher(oldCourse.getTeacher());
        newCourse.setTitle(oldCourse.getTitle());
        newCourse.setSubject(oldCourse.getSubject());
        newCourse.setDescription(oldCourse.getDescription());
        newCourse.setDateStart(oldCourse.getDateStart());
        newCourse.setThematics(oldCourse.getThematics());
    }

    @Override
    public List<CourseEntity> findAll() {
        return courseRepo.findAll();
    }

    @Override
    public void addThematictoCourse(long idCourse, long idThematic) {
        CourseEntity course =courseRepo.findById(idCourse).get();
        ThematicEntity thematic=thematicRepo.findById(idThematic).get();
        course.getThematics().add(thematic);
    }


}
