package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.example.demo.Entity.*;
import com.example.demo.SlopeOne.SlopeOne;
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
import java.time.LocalDateTime;
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
    private final RoleRepo roleRepo;
    private final RegistrationRepo registrationRepo;
    private final SlopeOne slopeOne;

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
        courseEntity.setTitle(course.getTitle());
        courseEntity.setSubject(subjectRepo.findByCode(course.getSubject()).get());
        courseEntity.setDescription(course.getDescription());
        courseEntity.setDateStart(course.getDateTime());
        courseEntity.setApproved("PENDDING");
        courseEntity.setLinkVideoIntro(fileName);
        courseEntity.setRegisterDate(LocalDateTime.now());
        try{

            fileStore.save(pathFile,fileName,Optional.of(metadataFile),course.getFile().getInputStream());
            fileStore.save(pathThumdnail,thumdnailName,Optional.of(metadataThumdnail),course.getThumdnail().getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        RegistrationsEntity registrationsEntity = new RegistrationsEntity();
        registrationsEntity.setCourse(courseRepo.save(courseEntity));
        UserEntity user = userRepo.findByUsername(course.getUserName()).get();
        registrationsEntity.setUser(user);
        registrationsEntity.setRegistrationDate(LocalDateTime.now());
        registrationsEntity.setRole(roleRepo.findByName("TEACHER"));
        registrationRepo.save(registrationsEntity);
        return courseEntity;
    }

    private static Map<String, String> getStringStringMap(MultipartFile file) {
        Map<String, String> metadata= new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));
        return metadata;
    }

    @Override
    public void saveCourseDetail(Long id,CourseDetailRequest courseDetailRequest) {
        if(courseDetailRequest.getThematics()!=null){
            CourseEntity courseEntity = courseRepo.findById(id).get();
            List<ThematicEntity> thematics = getThematicEntities(courseDetailRequest.getThematics());
            Iterable<ThematicEntity> itrThem=thematics;
            thematicRepo.saveAll(itrThem);
            courseEntity.setThematics(thematics);
            courseRepo.save(courseEntity);
        }
    }

    private List<ThematicEntity> getThematicEntities(List<ThematicRequest> thematicRequests) {
        List<ThematicEntity> thematics =new ArrayList<>();
        for(ThematicRequest thematic: thematicRequests){
            ThematicEntity thematicEntity = new ThematicEntity();
            thematicEntity.setName(thematic.getName());
            List<LectureEntity> lectures = new ArrayList<>();
            if(thematic.getLectures()!=null){
                for(LectureRequest lecture:thematic.getLectures()){
                    LectureEntity lectureEntity= new LectureEntity();
                    lectureEntity.setTitle(lecture.getTitle());
                    lectureEntity.setLocked(true);
                    lectures.add(lectureEntity);
                }
                thematicEntity.setLectures(lectures);
                Iterable<LectureEntity> itrLect= lectures;
                lectureRepo.saveAll(itrLect);
            }

            thematics.add(thematicEntity);
        }
        return thematics;
    }

    @Override
    public ListCourseResponse findAllByFilter(Map<String, Object> params) {
        ListCourseResponse listCourseResponse =new ListCourseResponse();
        ObjectFilter objectFilter =new ObjectFilter();
        if(params.containsKey("status") && !params.get("status").equals(""))
            objectFilter.setStatus(Boolean.valueOf((String)params.get("status")));

        if(params.containsKey("stringQuery")) objectFilter.setStringQuery((String)params.get("stringQuery"));
        else objectFilter.setStringQuery("");
        if(params.containsKey("sort")) objectFilter.setSort((String)params.get("sort"));
        else objectFilter.setSort("desc");
        if(params.containsKey("subject")) objectFilter.setSubject((String)params.get("subject"));
        else objectFilter.setSubject("");
        if(params.containsKey("program")) objectFilter.setProgram((String)params.get("program"));
        else objectFilter.setProgram("");
        if(params.containsKey("category")) objectFilter.setCategory((String)params.get("category"));
        else objectFilter.setCategory("");
        if(params.containsKey("approved")) objectFilter.setApproved((String)params.get("approved"));
        else objectFilter.setApproved("");

        int totalItem=0;
        if(objectFilter.getStatus()!=null){
            totalItem =courseRepo.findAllByFilter("%"+
                            objectFilter.getCategory()+"%",
                    "%"+objectFilter.getProgram()+"%",
                    "%"+objectFilter.getSubject()+"%",
                    "%"+objectFilter.getStringQuery()+"%",
                    objectFilter.getApproved()+"%",
                    objectFilter.getStatus(),
                    0,
                    100).size();
        }else {
            totalItem =courseRepo.findAllByFilterWithoutStatus("%"+
                            objectFilter.getCategory()+"%",
                    "%"+objectFilter.getProgram()+"%",
                    "%"+objectFilter.getSubject()+"%",
                    "%"+objectFilter.getStringQuery()+"%",
                    objectFilter.getApproved()+"%",
                    0,
                    100).size();
        }

        if(params.containsKey("page")){
            if((Integer.parseInt((String)params.get("page")))*(Integer.parseInt((String)params.get("rowsPerPage")))<=totalItem)
                listCourseResponse.setNextPage(Integer.parseInt((String)params.get("page"))+1);
            listCourseResponse.setPreviousPage(Integer.parseInt((String)params.get("page")));
            if(params.containsKey("rowsPerPage"))
                objectFilter.setRowsPerPage(Integer.parseInt((String)params.get("rowsPerPage")));
            else
                objectFilter.setRowsPerPage(10);
            objectFilter.setOffSet((Integer.parseInt((String)params.get("page"))-1)*objectFilter.getRowsPerPage());
        }else{
            listCourseResponse.setNextPage(2);
            listCourseResponse.setPreviousPage(1);
            objectFilter.setOffSet(0);
            objectFilter.setRowsPerPage(50);
        }
        List<CourseEntity> courseEntityList= new ArrayList<>();
        if(objectFilter.getStatus()!=null){
          courseEntityList=  courseRepo.findAllByFilter("%"+
                            objectFilter.getCategory()+"%",
                    "%"+objectFilter.getProgram()+"%",
                    "%"+objectFilter.getSubject()+"%",
                    "%"+objectFilter.getStringQuery()+"%",
                    objectFilter.getApproved()+"%",
                    objectFilter.getStatus(),
                    objectFilter.getOffSet(),
                    objectFilter.getRowsPerPage()
            );
        }else{
            courseEntityList=  courseRepo.findAllByFilterWithoutStatus("%"+
                            objectFilter.getCategory()+"%",
                    "%"+objectFilter.getProgram()+"%",
                    "%"+objectFilter.getSubject()+"%",
                    "%"+objectFilter.getStringQuery()+"%",
                    objectFilter.getApproved()+"%",
                    objectFilter.getOffSet(),
                    objectFilter.getRowsPerPage()
            );
        }
        listCourseResponse.setListCourse(courseEntityList);

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
             pathFile = String.format("%s/%s", BucketName.COURSE_VIDEO.getBucketName(), newCourse.getTitle());
             fileName =String.format("%s-%s",courseUpdateRequest.getFile().getName(),newCourse.getTitle());
             newCourse.setLinkVideoIntro(fileName);
             try{
                 if(oldCourse.getLinkVideoIntro()!= null)
                 fileStore.deleteFile( BucketName.COURSE_VIDEO.getBucketName(),oldCourse.getLinkVideoIntro());
                 fileStore.save(pathFile,fileName,Optional.of(metadata),courseUpdateRequest.getFile().getInputStream());
             }catch (IOException e) {
                 throw new RuntimeException(e);
             }
        }
        if(courseUpdateRequest.getThumnail()!=null){
            Map<String, String> metadata= getStringStringMap(courseUpdateRequest.getThumnail());
            pathThumdnail = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), newCourse.getTitle());
            thumdnailName =String.format("%s-%s",courseUpdateRequest.getThumnail().getName(),newCourse.getTitle());
            newCourse.setImage(thumdnailName);
            try{
                if(oldCourse.getImage()!=null)
                fileStore.deleteFile( BucketName.COURSE_IMAGE.getBucketName(),oldCourse.getImage());
                fileStore.save(pathThumdnail,thumdnailName,Optional.of(metadata),courseUpdateRequest.getThumnail().getInputStream());
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(courseUpdateRequest.getSubject()!=null){
            newCourse.setSubject(subjectRepo.findByCode(courseUpdateRequest.getSubject()).get());
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

    @Override
    public CourseEntity lock(Long id, Boolean status) {
        CourseEntity course = courseRepo.findById(id).get();
        course.setStatus(status);
        return courseRepo.save(course);
    }

    @Override
    public Page<CourseResponse> getAllCourse(Map<String, Object> params) {
        int page =0;
        int size =10;
        if(params.containsKey("page") && params.get("page")!= null && !params.get("page").toString().isEmpty()) page = Integer.parseInt(params.get("page").toString());
        if(params.containsKey("rowsPerPage") && params.get("page")!=null && !params.get("rowsPerPage").toString().isEmpty()) size = Integer.parseInt(params.get("rowsPerPage").toString());
        Page<CourseResponse> courseResponses = courseRepo.getAllCourses(PageRequest.of(page,size));
        List<CourseResponse> list = courseResponses.getContent();

       for(CourseResponse course: list){
           String path = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), course.getUserName());
           String key =course.getAvatarTeacher();
           if(key==null){
               continue;
           }
           try {
                course.setAvatar(IOUtils.toByteArray(fileStore.getObject(path,key).getObjectContent()));
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
        return courseResponses;
    }

    @Override
    public Page<CourseResponse> search(Map<String, Object> params) {
        int page =0;
        int size =10;
        String search="";
        if(params.containsKey("page") && params.get("page")!= null && !params.get("page").toString().isEmpty()) page = Integer.parseInt(params.get("page").toString());
        if(params.containsKey("rowsPerPage") && params.get("page")!=null && !params.get("rowsPerPage").toString().isEmpty()) size = Integer.parseInt(params.get("rowsPerPage").toString());
        if(params.containsKey("search") && params.get("search")!= null ) search =String.valueOf(params.get("search"));
        Page<CourseResponse> courseResponses = courseRepo.search(PageRequest.of(page,size),"%"+search+"%");
        return courseResponses;
    }

    @Override
    public List<CourseResponse> getCourseRecommend(String userName) {
        List<CourseResponse> responses = new ArrayList<>();
        UserEntity user = userRepo.findByUsername(userName).get();
        List<CourseEntity> listCourseEntity =slopeOne.slopeOne(user);
        for(CourseEntity courseEntity: listCourseEntity){
            CourseResponse courseResponse = new CourseResponse();
            courseResponse.setId(courseEntity.getId());
            courseResponse.setApproved(courseEntity.getApproved());
            courseResponse.setImage(courseEntity.getImage());
            courseResponse.setPrice(courseEntity.getPrice());
            courseResponse.setTitle(courseEntity.getTitle());
            courseResponse.setStatus(courseEntity.isStatus());
            courseResponse.setShortDescription(courseEntity.getShortDescription());
            UserEntity teacher = userRepo.findTeacherOfCourse(courseEntity.getId());
            courseResponse.setUserName(teacher.getUsername());
            courseResponse.setFullNameTeacher(teacher.getFullname());
            String path = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), teacher.getUsername());
            String key =teacher.getAvatar();
            if(key!=null){
                try {
                    courseResponse.setAvatar(IOUtils.toByteArray(fileStore.getObject(path,key).getObjectContent()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            responses.add(courseResponse);

        }
        return responses;
    }

    @Override
    public List<CourseHistoryResponse> getHistory(Long idUser) {
        return courseRepo.getHistory(idUser);
    }

    @Override
    public List<CourseResponse> getCourseEnrolled(String userName) {
        UserEntity user = userRepo.findByUsername(userName).get();
        List<CourseResponse> list = courseRepo.getCourseEnrolled(user.getId());
        for(CourseResponse courseResponse: list){
            UserEntity teacher = userRepo.findTeacherOfCourse(courseResponse.getId());
            courseResponse.setIdTeacher(teacher.getId());
            courseResponse.setFullNameTeacher(teacher.getFullname());
        }
        return list;
    }

    private static void extracted(CourseEntity oldCourse, CourseEntity newCourse) {
        newCourse.setId(oldCourse.getId());
        newCourse.setImage(oldCourse.getImage());
        newCourse.setPrice(oldCourse.getPrice());
        newCourse.setTitle(oldCourse.getTitle());
        newCourse.setSubject(oldCourse.getSubject());
        newCourse.setDescription(oldCourse.getDescription());
        newCourse.setDateStart(oldCourse.getDateStart());
        newCourse.setThematics(oldCourse.getThematics());
        newCourse.setApproved(oldCourse.getApproved());
        newCourse.setApprovedDate(oldCourse.getApprovedDate());
        newCourse.setStatus(oldCourse.isStatus());
        newCourse.setShortDescription(oldCourse.getShortDescription());
        newCourse.setRegisterDate(oldCourse.getRegisterDate());
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
