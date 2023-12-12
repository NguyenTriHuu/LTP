package com.example.demo.api.web;

import com.example.demo.Entity.EducationProgramEntity;
import com.example.demo.dto.ProgramRequest;
import com.example.demo.service.ProgramEducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class ProgramApi {
    private final ProgramEducationService programEducationService;

    @GetMapping(value = "/program/all")
    public ResponseEntity<List<ProgramRequest>> getAll(){
        return ResponseEntity.ok().body(programEducationService.getAll());
    }

    @GetMapping(value = "/program/{idCategory}")
    public ResponseEntity<List<EducationProgramEntity>> getByCategory(@PathVariable("idCategory") Long id){
        return ResponseEntity.ok().body(programEducationService.getByCategory(id));
    }

    @PostMapping(value = "/program/save")
    public ResponseEntity<EducationProgramEntity> save(@RequestBody ProgramRequest programRequest){
        if(programRequest.getId()!=null)return ResponseEntity.ok().body(programEducationService.update(programRequest));
        return ResponseEntity.ok().body(programEducationService.save(programRequest));
    }


}
