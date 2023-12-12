package com.example.demo.SlopeOne;

import java.util.*;

import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.RatingEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.repo.CourseRepo;
import com.example.demo.repo.RatingRepo;
import com.example.demo.repo.UserRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class InputData {
   private final CourseRepo courseRepo;
    protected static List<Item> items = new ArrayList<>();
    private final UserRepo userRepo;
    private final RatingRepo ratingRepo;
    public  void setItem(){
       List<CourseEntity> list =courseRepo.findAll();
       for(CourseEntity course :list){
           items.add(new Item(String.valueOf(course.getId())));
       }
    }
    public  Map<User, HashMap<Item, Double>> initializeData(UserEntity userEntity) {
        setItem();
        Map<User, HashMap<Item, Double>> data = new HashMap<>();
        HashMap<Item, Double> newUser;
        Set<Item> newRecommendationSet;
        List<UserEntity> listUser = userRepo.findAllBySlopeOne(userEntity.getDateOfBirth().getYear());
        for(UserEntity user :listUser){
            newUser = new HashMap<Item, Double>();
            newRecommendationSet = new HashSet<>();
            HashMap<String,Double> ratesOfUser= new HashMap<>();
            List<RatingEntity> rates = ratingRepo.findAllRateOfUserForCourse(user.getId());
            for (RatingEntity rate : rates) {
                newRecommendationSet.add(new Item(String.valueOf(rate.getCourse().getId())));
                ratesOfUser.put(String.valueOf(rate.getCourse().getId()), Double.valueOf( String.valueOf(rate.getRating()))/5 );
            }
            for (Item item : newRecommendationSet) {
                double rating =ratesOfUser.get(""+item.getItemName());
                newUser.put(item, rating);
            }
            data.put(new User("User " + user.getId()), newUser);
        }
        return data;
    }
}
