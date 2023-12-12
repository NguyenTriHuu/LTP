package com.example.demo.api.web;

import com.example.demo.dto.SubjectReqRes;
import com.example.demo.service.SubjectService;
import com.example.demo.service.SubjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class SubjectApi {
    private final SubjectService subjectService;
    @GetMapping(value = "/subject/all")
    public ResponseEntity<List<SubjectReqRes>> getAll(){
        return ResponseEntity.ok().body(subjectService.getAll());
    }

    @GetMapping(value = "/subject/{idProgram}")
    public ResponseEntity<List<SubjectReqRes>> getSubjectByProgram(@PathVariable("idProgram") Long id){
        return ResponseEntity.ok().body(subjectService.getByProgram(id));
    }

    @PostMapping(value = "/subject/save")
    public ResponseEntity<SubjectReqRes> save(@RequestBody SubjectReqRes subjectReqRes){
        if(subjectReqRes.getId()!=null) return ResponseEntity.ok().body(subjectService.update(subjectReqRes));
        return ResponseEntity.ok().body(subjectService.save(subjectReqRes));
    }


}
