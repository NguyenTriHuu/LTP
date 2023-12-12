package com.example.demo.service;

import com.example.demo.dto.RatingRequest;

public interface RatingService {
    RatingRequest save(RatingRequest ratingRequest);

    RatingRequest findRatingCourse(String userName, Long idCourse);
}
