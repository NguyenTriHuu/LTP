package com.example.demo.repo;

import com.example.demo.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity,Long> , JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByUsername(String userName);

    @Query(value = "select * from user join user_roles on user.id = user_entity_id join role on user_roles.roles_id = role.id where role.name='TEACHER'", nativeQuery=true)
    List<UserEntity> findAllByIsTeacher();

    //@Query(value = "select * from user ")
    //List<UserEntity> getAllUser (@Param("id") Long id);

    @Query(value = "select * from user join user_roles on user.id =user_entity_id join role on user_roles.roles_id =role.id where year(user.date_of_birth) =:year and role.name='STUDENT'",nativeQuery = true)
    List<UserEntity> findAllBySlopeOne(@Param("year") int year);


    @Modifying
    @Query(value = "delete from user_roles where user_entity_id =?1", nativeQuery = true)
    void deleteRoleOfUser(Long idUser);

    @Query(value = "select * from user join registration on user.id = registration.user_id where course_id =?1 and role_id =3",nativeQuery = true)
    UserEntity findTeacherOfCourse(Long idCourse);
}
