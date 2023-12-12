package com.example.demo.service;

import com.example.demo.Entity.BlogEntity;
import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.RatingEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.RatingRequest;
import com.example.demo.repo.BlogRepo;
import com.example.demo.repo.CourseRepo;
import com.example.demo.repo.RatingRepo;
import com.example.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RatingServiceImpl implements RatingService{
    private final RatingRepo ratingRepo;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final BlogRepo blogRepo;
    @Override
    public RatingRequest save(RatingRequest ratingRequest) {
        RatingEntity rating = new RatingEntity();
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        Date date = new Date(timestamp.getTime());
        if(ratingRequest.getIdRating()!=null){
            rating=ratingRepo.findById(ratingRequest.getIdRating()).get();
            rating.setModifiedtime(date);
        }else {
            rating.setCreatedtime(date);
        }
        rating.setRating((double)ratingRequest.getRating());
        UserEntity user = userRepo.findByUsername(ratingRequest.getUserName()).get();
        rating.setUser(user);
        if(ratingRequest.getIdCourse()!=null){
            CourseEntity course = courseRepo.findById(ratingRequest.getIdCourse()).get();
            rating.setCourse(course);
        }
        if(ratingRequest.getIdBlog()!=null){
            BlogEntity blog = blogRepo.findById(ratingRequest.getIdBlog()).get();
            rating.setBlog(blog);
        }

        RatingEntity ratingSave= ratingRepo.save(rating);
        ratingRequest.setIdRating(ratingSave.getId());
        return ratingRequest;
    }

    @Override
    public RatingRequest findRatingCourse(String userName, Long idCourse) {
        UserEntity user = userRepo.findByUsername(userName).get();
        RatingEntity rating = ratingRepo.findRatingCourse(idCourse,user.getId());
        RatingRequest ratingRequest = new RatingRequest();
        if(rating!=null){
           ratingRequest.setIdRating(rating.getId());
           ratingRequest.setIdCourse(idCourse);
           ratingRequest.setUserName(userName);
           ratingRequest.setRating(rating.getRating().intValue());
        }
        return ratingRequest;
    }
}
