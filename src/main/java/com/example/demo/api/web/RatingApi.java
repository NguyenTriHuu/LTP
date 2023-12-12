package com.example.demo.api.web;

import com.example.demo.dto.RatingRequest;
import com.example.demo.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class RatingApi {
    private final RatingService ratingService;

    @PostMapping(value = "/rating/save")
    public ResponseEntity<RatingRequest> save (@RequestBody RatingRequest ratingRequest){
        return ResponseEntity.ok().body(ratingService.save(ratingRequest));
    }

    @GetMapping(value = "/rating/course/{idCourse}/get")
    public ResponseEntity<RatingRequest> getRatingCourse(@PathVariable("idCourse") String id,@RequestParam String userName){
        return ResponseEntity.ok().body(ratingService.findRatingCourse(userName,Long.parseLong(id)));
    }
}
