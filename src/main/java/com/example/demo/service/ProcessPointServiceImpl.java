package com.example.demo.service;

import com.example.demo.Entity.*;
import com.example.demo.dto.ProcessResponse;
import com.example.demo.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProcessPointServiceImpl implements ProcessPointService{
    private final CourseRepo courseRepo;
    private final UserRepo userRepo;
    private final ProcessPointRepo processPointRepo;
    private final FileRepo fileRepo;
    private final LectureRepo lectureRepo;
    private final RoleRepo roleRepo;
    private final RegistrationRepo registrationRepo;
    @Override
    public void save(Long idCourse, Long idUser) {
        CourseEntity course =courseRepo.findById(idCourse).get();
        UserEntity user =userRepo.findById(idUser).get();
        if(processPointRepo.findByCourseAndUser(course.getId(),user.getId()).get()==null){
            ProcessPointEntity processPointEntity =new ProcessPointEntity();
            processPointEntity.setCourse(course);
            processPointEntity.setUser(user);
            processPointEntity.setPoint(0);
            ThematicEntity thematic =course.getThematics().iterator().next();
            LectureEntity lecture =thematic.getLectures().iterator().next();
            processPointEntity.setStudying(lecture.getId());
        }
    }

    @Override
    public ProcessResponse next(Long idCourse, String userName) {
        ProcessResponse processResponse =new ProcessResponse();
        Long idUser = userRepo.findByUsername(userName).get().getId();
        ProcessPointEntity processPoint =processPointRepo.findByCourseAndUser(idCourse,idUser).get();
        LectureEntity lectureOld = lectureRepo.findById(processPoint.getStudying()).get();
        if(processPoint.getPoint()<100){
            processPoint.getLessonLearned().add(lectureOld);
            int i = (processPoint.getLessonLearned().size()*100 / countFile(processPoint.getCourse()));
            processPoint.setPoint(i);
        }
        List<LectureEntity> listLecture =arrayLecture(processPoint.getCourse());
        int idx = listLecture.indexOf(lectureOld);
        if(idx >=0){
            int idxNext =idx +1;
            if(idxNext< listLecture.size()){
                processPoint.setStudying(listLecture.get(idxNext).getId());
            }else{
                processPoint.setStudying(listLecture.get(0).getId());
            }
        }
        processResponse.setStudying(processPoint.getStudying());
        List <Long> ids = new ArrayList<>();
        for(LectureEntity lecture: processPoint.getLessonLearned()){
            ids.add(lecture.getId());
        }
        processResponse.setLearned(ids);
        processResponse.setPoint(processPoint.getPoint());

        processPointRepo.save(processPoint);
        return  processResponse;
    }

    @Override
    public ProcessResponse getPointCurrent(Long idCourse, String userName) {
        ProcessResponse processResponse =new ProcessResponse();
        Long idUser = userRepo.findByUsername(userName).get().getId();
        ProcessPointEntity processPoint =processPointRepo.findByCourseAndUser(idCourse,idUser).get();
        processResponse.setStudying(processPoint.getStudying());
        List <Long> ids = new ArrayList<>();
        for(LectureEntity lecture: processPoint.getLessonLearned()){
            ids.add(lecture.getId());
        }
        processResponse.setLearned(ids);
        processResponse.setPoint(processPoint.getPoint());
        return processResponse;
    }


    @Override
    public ProcessPointEntity getProcessPointByCourseAndUser(Long idCourse, Long idUser) {
        ProcessPointEntity processPointEntity =processPointRepo.findByCourseAndUser(idCourse,idUser).orElse(null);
        if(processPointEntity!=null){
            return processPointRepo.findByCourseAndUser(idCourse,idUser).get();
        }
        CourseEntity course =courseRepo.findById(idCourse).get();
        UserEntity user =userRepo.findById(idUser).get();
        RegistrationsEntity registrationsEntity = new RegistrationsEntity();
        registrationsEntity.setCourse(course);
        registrationsEntity.setUser(user);
        registrationsEntity.setRegistrationDate(LocalDateTime.now());
        registrationsEntity.setRole(roleRepo.findByName("STUDENT"));
        registrationRepo.save(registrationsEntity);
        ProcessPointEntity processPointEntity2 =new ProcessPointEntity();
        processPointEntity2.setCourse(course);
        processPointEntity2.setUser(user);
        processPointEntity2.setPoint(0);
        ThematicEntity thematic =course.getThematics().iterator().next();
        LectureEntity lecture =thematic.getLectures().iterator().next();
        processPointEntity2.setStudying(lecture.getId());

        return processPointRepo.save(processPointEntity2);
    }



    public static int countFile(CourseEntity courseEntity){
        int num =0;
        for(ThematicEntity thematic : courseEntity.getThematics()){
                num += thematic.getLectures().size();
        }
        return num;
    }

    public static List<LectureEntity> arrayLecture(CourseEntity courseEntity){
        List<LectureEntity> list =new ArrayList<>();
        for(ThematicEntity thematic: courseEntity.getThematics()){
            for(LectureEntity lecture: thematic.getLectures()){
                list.add(lecture);
            }
        }
        return list;
    }
}
