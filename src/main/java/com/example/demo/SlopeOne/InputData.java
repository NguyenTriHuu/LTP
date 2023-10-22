package com.example.demo.SlopeOne;

import java.util.*;

import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.UserClicksOnCourseEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.repo.CourseRepo;
import com.example.demo.repo.UserClickOnCourseRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.specifications.UserSpecification;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class InputData {
    private final CourseRepo courseRepo;
    protected static List<Item> items = new ArrayList<>();
    private final UserRepo userRepo;
    private final UserClickOnCourseRepo userClickOnCourseRepo;
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
        Specification<UserEntity> specification = UserSpecification.hasAge(userEntity.getAge()).and(UserSpecification.hasRole("STUDENT"));
        List<UserEntity> listUser = userRepo.findAll(specification);

        for(UserEntity user :listUser){
            Collection<UserClicksOnCourseEntity> clicks = user.getNumOfClick();
            newUser = new HashMap<Item, Double>();
            newRecommendationSet = new HashSet<>();
            int totalNumofClick=0;
            for (UserClicksOnCourseEntity click : clicks) {
                newRecommendationSet.add(new Item(String.valueOf(click.getCourse().getId())));
                totalNumofClick +=click.getValue();
            }
            for (Item item : newRecommendationSet) {
                UserClicksOnCourseEntity userClicksOnCourse = userClickOnCourseRepo.findUserClicksOnCourseEntities(user.getId(),(long)Integer.parseInt(item.getItemName()));
                double rating =(double) userClicksOnCourse.getValue()/totalNumofClick;
                newUser.put(item, rating);
            }
            data.put(new User("User " + user.getId()), newUser);
        }
        return data;
    }

}
