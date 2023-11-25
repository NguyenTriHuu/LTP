package com.example.demo.api.web;

import com.example.demo.Entity.CourseEntity;
import com.example.demo.dto.CourseDetailRequest;
import com.example.demo.dto.ExerciseRequet;
import com.example.demo.dto.ThematicRequest;
import com.example.demo.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class ExerciseApi {
    private final ExerciseService exerciseService;
    @PostMapping(value = "/course/lecture/{idLecture}/exercise/save")
    public void SaveCourseDetail(@PathVariable("idLecture") Long id,
                                                         @RequestBody List<ExerciseRequet<String>> data) throws IllegalAccessException {

        if(data !=null ){
            exerciseService.save(id,data);
        }
    }

    @GetMapping(value = "/course/lecture/{idLecture}/exercises/get")
    public ResponseEntity<List<ExerciseRequet<Long>>> getExercises (@PathVariable("idLecture") Long id){
        return ResponseEntity.ok().body(exerciseService.getExercises(id));
    }

        @GetMapping(value = "/course/lecture/{idLecture}/exercises/noanswer/get")
    public ResponseEntity<List<ExerciseRequet<Long>>> getExercisesNoAnswer (@PathVariable("idLecture") Long id){
        return ResponseEntity.ok().body(exerciseService.getExercisesNoAnswer(id));
    }
}
