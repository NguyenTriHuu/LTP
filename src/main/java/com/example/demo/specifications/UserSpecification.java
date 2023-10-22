package com.example.demo.specifications;

import com.example.demo.Entity.RoleEntity;
import com.example.demo.Entity.UserClicksOnCourseEntity;
import com.example.demo.Entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public class UserSpecification {

    public static Specification<UserEntity> hasAge(int age){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("age"),age));
    }

    public static Specification<UserEntity> hasRole(String role){
        return ((root, query, criteriaBuilder) -> {
            Join<RoleEntity,UserEntity> userRole= root.join("roles");
            return criteriaBuilder.equal(userRole.get("name"),role);
        });
    }


}
