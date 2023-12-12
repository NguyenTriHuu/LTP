package com.example.demo.repo;

import com.example.demo.Entity.CourseEntity;
import com.example.demo.dto.CourseHistoryResponse;
import com.example.demo.dto.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<CourseEntity,Long> {
        Optional<CourseEntity> findById(Long id);
        CourseEntity findByTitle(String name);
        @Query(value =
                "select course.id, course.title, course.createdtime,course.date_start,course.description,course.image,course.modifiedtime,course.price,course.status,course.subject_id,course.approved,course.approved_date,course.register_date , course.short_description ,course.link_video_intro " +
                "from categoty join categoty_programs on categoty.id = categoty_programs.category_entity_id \n" +
                "join program on  categoty_programs.programs_id =program.id \n" +
                "join program_subjects on program.id =program_subjects.education_program_entity_id \n" +
                "join subject on program_subjects.subjects_id=subject.id \n" +
                "join course  on subject.id = course.subject_id \n" +
                "where LOWER(categoty.code) like lower(?1)  and lower(program.code) like lower(?2) and lower(subject.code) like lower(?3)" +
                "and lower(course.title) like lower(?4) and lower(course.approved) like lower(?5)  and course.status =?6 order by course.date_start desc limit ?7,?8",nativeQuery = true)
        List<CourseEntity> findAllByFilter(String category,
                                           String program,
                                           String subject,
                                           String stringQuery,
                                           String approved,
                                           boolean status,
                                           int offSet,
                                           int page);

        @Query(value =
                "select course.id, course.title, course.createdtime,course.date_start,course.description,course.image,course.modifiedtime,course.price,course.status,course.subject_id,course.approved,course.approved_date,course.register_date , course.short_description ,course.link_video_intro " +
                        "from categoty join categoty_programs on categoty.id = categoty_programs.category_entity_id \n" +
                        "join program on  categoty_programs.programs_id =program.id \n" +
                        "join program_subjects on program.id =program_subjects.education_program_entity_id \n" +
                        "join subject on program_subjects.subjects_id=subject.id \n" +
                        "join course  on subject.id = course.subject_id \n" +
                        "where LOWER(categoty.code) like lower(?1)  and lower(program.code) like lower(?2) and lower(subject.code) like lower(?3)" +
                        "and lower(course.title) like lower(?4) and lower(course.approved) like lower(?5) and course.approved='APPROVED' order by course.date_start desc limit ?6,?7",nativeQuery = true)
        List<CourseEntity> findAllByFilterWithoutStatus(String category, String program, String subject, String stringQuery, String approved, int offSet, int page);
        @Query(value = "select * from course where title like %?1% order by title desc limit 1,2",nativeQuery = true)
        List<CourseEntity> test(String title);


        @Query("SELECT new com.example.demo.dto.CourseResponse(c.approved, c.shortDescription, c.id, c.image, c.price, c.status, c.title, u.avatar, u.fullname,u.username) FROM CourseEntity c JOIN RegistrationsEntity r ON c.id = r.course.id JOIN UserEntity u ON r.user.id = u.id JOIN u.roles role WHERE role.id = 3 AND c.approved = 'APPROVED'")
        Page<CourseResponse> getAllCourses(Pageable pageable);

        @Query("select new com.example.demo.dto.CourseHistoryResponse(c.id,c.title,re.registrationDate,c.price)from CourseEntity c join RegistrationsEntity re on c.id=re.course.id where re.user.id =?1 and re.role.id=2 order by re.registrationDate desc")
        List<CourseHistoryResponse> getHistory(Long idUser);

        @Query("select new com.example.demo.dto.CourseResponse(c.shortDescription,c.id,c.price,c.status,c.title)from CourseEntity c join RegistrationsEntity re on c.id=re.course.id where re.user.id =?1 and re.role.id=2 order by re.registrationDate desc")
        List<CourseResponse> getCourseEnrolled(Long idUser);

        @Query("SELECT new com.example.demo.dto.CourseResponse(c.approved, c.shortDescription, c.id, c.image, c.price, c.status, c.title, u.avatar, u.fullname,u.username) FROM CourseEntity c JOIN RegistrationsEntity r ON c.id = r.course.id JOIN UserEntity u ON r.user.id = u.id JOIN u.roles role WHERE role.id = 3 AND c.approved = 'APPROVED' and lower(c.title) like lower(?1) ")
        Page<CourseResponse> search(Pageable pageable,String search);
}
