package com.example.demo.api.web;

import com.example.demo.Entity.ProcessPointEntity;
import com.example.demo.dto.ProcessResponse;
import com.example.demo.service.ProcessPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class ProcessPointApi {
    private final ProcessPointService processPointService;
    @GetMapping(value = "/course/processpoint/next")
    public ResponseEntity<ProcessResponse> Next (@RequestParam Long idCourse, @RequestParam String userName){
        return ResponseEntity.ok().body(processPointService.next(idCourse,userName));
    }

    @GetMapping(value = "/course/processpoint/pointcurrent")
    public ResponseEntity<ProcessResponse> Current (@RequestParam Long idCourse, @RequestParam String userName){
        return ResponseEntity.ok().body(processPointService.getPointCurrent(idCourse,userName));
    }

}
