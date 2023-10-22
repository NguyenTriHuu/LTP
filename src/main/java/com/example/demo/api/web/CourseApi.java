package com.example.demo.api.web;

import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.TestCourse;
import com.example.demo.Entity.UserEntity;
import com.example.demo.SlopeOne.Item;
import com.example.demo.SlopeOne.SlopeOne;
import com.example.demo.dto.*;
import com.example.demo.s3.FileStore;
import com.example.demo.service.CourseService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class CourseApi {
    private final CourseService courseService;
    private final SlopeOne slopeOne;
    private final UserService userService;
    private  final TestCourse testCourse;
    private final FileStore fileStore;
    @GetMapping("/course")
    public ResponseEntity<CourseEntity> GetCourse(@RequestParam Long id) throws IllegalAccessException {
        return  ResponseEntity.ok().body(courseService.findCourseById(id));
    }

    @GetMapping("/courses/rated")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<Collection<CourseEntity>> GetCourseRated(@RequestParam String userName) throws IllegalAccessException {
        UserEntity userEntity = userService.getUser(userName);
        List<CourseEntity> listCourse =new ArrayList<>();

            listCourse =slopeOne.slopeOne(userEntity);

        try{
            for(CourseEntity course:listCourse){
                System.out.println(course.getTitle());
            }
        }catch (NullPointerException e){
            System.out.println("excep ra dc "+e);
            return ResponseEntity.noContent().build();
        }


        return ResponseEntity.ok().body(listCourse);
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

    @PostMapping(value = "/course/save",
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
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
}
