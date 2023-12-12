package com.example.demo.api.web;

import com.amazonaws.services.s3.model.S3Object;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.LectureEntity;
import com.example.demo.Entity.ProcessPointEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.SlopeOne.SlopeOne;
import com.example.demo.dto.*;
import com.example.demo.repo.LectureRepo;
import com.example.demo.s3.FileStore;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class CourseApi {
    private final CourseService courseService;
    private final SlopeOne slopeOne;
    private final UserService userService;
    private final FileStore fileStore;
    private final ProcessPointService processPointService;
    private final RegistrationCourseService registrationCourseService;
    private final LectureService lectureService;
    @GetMapping("/course")
    public ResponseEntity<CourseEntity> GetCourse(@RequestParam Long id) throws IllegalAccessException {
        return  ResponseEntity.ok().body(courseService.findCourseById(id));
    }

    @GetMapping("/courses/rated")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<List<CourseResponse>> GetCourseRated(@RequestParam String userName) throws IllegalAccessException {
        return ResponseEntity.ok().body(courseService.getCourseRecommend(userName));
    }



    @GetMapping("/courses/all")
    //@PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<List<CourseEntity>> GetCourse() throws IllegalAccessException {
        ArrayList<CourseEntity> listCourses= new ArrayList<>();
        listCourses = (ArrayList<CourseEntity>) courseService.findAll();
        return ResponseEntity.ok().body(listCourses);
    }

    @GetMapping("/courses")
    //@PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<ListCourseResponse> GetCourseByFilter(@RequestParam(required = false) Map<String, Object> params) throws IllegalAccessException {
        ListCourseResponse listCourseResponse =courseService.findAllByFilter(params);
        return ResponseEntity.ok().body(listCourseResponse);
    }

    @GetMapping("/courses/list")
    //@PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<Page<CourseResponse>> getAllCourse(@RequestParam(required = false) Map<String, Object> params) throws IllegalAccessException {

        return ResponseEntity.ok().body(courseService.getAllCourse(params));
    }

    @GetMapping("/courses/search")
    //@PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<Page<CourseResponse>> search(@RequestParam(required = false) Map<String, Object> params) throws IllegalAccessException {

        return ResponseEntity.ok().body(courseService.search(params));
    }

    @PostMapping(value = "/course/save",
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
   // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseEntity> SaveCourse( 
            @ModelAttribute CourseSaveRequest course,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dateStart) throws IllegalAccessException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/course/save").toUriString());
        course.setDateTime(dateStart);
        return ResponseEntity.created(uri).body(courseService.save(course));
    }

    @PostMapping(value = "/course/save/{courseId}/detail")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseEntity> SaveCourseDetail(@PathVariable("courseId") String id,
                                                           @RequestBody List<ThematicRequest> course) throws IllegalAccessException {
        CourseDetailRequest courseDetailRequest = new CourseDetailRequest(course);
        courseService.saveCourseDetail(Long.parseLong(id),courseDetailRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/course/test")
    //@PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<Page<CourseEntity>> test(@RequestParam(required = false) Map<String, Object> params) throws IllegalAccessException {
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/course/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
  //  @PreAuthorize("hasAuthority('ADMIN')")
   public ResponseEntity<CourseEntity> update(@ModelAttribute CourseUpdateRequest course,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                              LocalDateTime dateStart) throws JsonProcessingException {
        course.setDateTime(dateStart);
        return ResponseEntity.ok().body(courseService.update(course));
    }

    @GetMapping(value = "/course/thumdnail/{id}/download")
    public byte[]  downloadthumdnail (@PathVariable("id") Long id){
        return courseService.downloadThumdnail(id);
    }

    @GetMapping(value = "/course/video/{id}/download")
    public ResponseEntity<InputStreamResource>  downloadVideo (@PathVariable("id") Long id){
        S3Object object =courseService.downloadVideo(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(object.getObjectMetadata().getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + object.getKey() + "\"")
                .body(new InputStreamResource(courseService.downloadVideo(id).getObjectContent()));
    }

    @GetMapping(value = "/course/video/{id}/download/url")
    public String  getVideoUrl (@PathVariable("id") Long id){
        return courseService.getVideoUrl(id);
    }

    @GetMapping(value = "/course/enrolled/{id}")
    public ResponseEntity<ProcessPointEntity> getProcess(@PathVariable("id") Long id,@RequestHeader(AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm= Algorithm.HMAC256("secrect".getBytes());
        JWTVerifier verifier= JWT.require(algorithm).build();
        DecodedJWT decodedJWT= verifier.verify(token);
        String username=decodedJWT.getSubject();
        return ResponseEntity.ok().body(processPointService.getProcessPointByCourseAndUser(id,userService.getUser(username).getId()));
    }

    @GetMapping(value = "/course/enrolled/check/{id}")
    public ResponseEntity<Boolean> checkEnrolled(@PathVariable("id") Long id,@RequestHeader(AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm= Algorithm.HMAC256("secrect".getBytes());
        JWTVerifier verifier= JWT.require(algorithm).build();
        DecodedJWT decodedJWT= verifier.verify(token);
        String username=decodedJWT.getSubject();
        return ResponseEntity.ok().body(registrationCourseService.checkIsStudent(username,id));
    }

    @GetMapping(value = "/course/lecture/get/{id}")
    public ResponseEntity<LectureEntity> getProcess(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(lectureService.getLecture(id));
    }

    @PostMapping(value = "/course/{id}/lock")
    public ResponseEntity<CourseEntity> ClockOrUnClockCourse(@PathVariable("id") Long id, @RequestBody Boolean status){
            return ResponseEntity.ok().body(courseService.lock(id,status));
    }

    @GetMapping(value = "/course/history/{userName}")
    public ResponseEntity<List<CourseHistoryResponse>> getHistory(@PathVariable("userName") String userName){
        UserEntity user = userService.getUser(userName);
        return ResponseEntity.ok().body(courseService.getHistory(user.getId()));
    }

    @GetMapping(value = "/course/get/registration/{userName}")
    public ResponseEntity<List<CourseResponse>> getCourseEnrolled(@PathVariable("userName")String userName){
         return ResponseEntity.ok().body(courseService.getCourseEnrolled(userName)) ;
    }


}
