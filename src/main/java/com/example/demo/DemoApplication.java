package com.example.demo;

import com.example.demo.Entity.*;
import com.example.demo.dto.ProfileTeacherUpdateRequest;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProgramEducationService;
import com.example.demo.service.SubjectService;
import com.example.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.ArrayList;

@SpringBootApplication
@EnableElasticsearchRepositories("com.example.demo.elasticsearch.Repository")
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	/*@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveUser(new UserEntity("user@gmail.com","123","join"));
			userService.saveUser(new UserEntity("admin@gmail.com","123","join1"));
			userService.saveUser(new UserEntity("manager@gmail.com","123","join2"));
			userService.saveUser(new UserEntity("teacher@gmail.com","123","join3"));
			userService.saveUser(new UserEntity("user2@gmail.com","123","join5"));
			userService.saveUser(new UserEntity("user3@gmail.com","123","join6"));
			userService.saveUser(new UserEntity("user4@gmail.com","123","join7"));
			userService.saveRole(new RoleEntity("ADMIN"));
			userService.saveRole(new RoleEntity("STUDENT"));
			userService.saveRole(new RoleEntity("TEACHER"));
			userService.saveRole(new RoleEntity("MANAGER"));

			userService.addRoleToUser("user2@gmail.com","STUDENT");
			userService.addRoleToUser("user3@gmail.com","STUDENT");
			userService.addRoleToUser("user4@gmail.com","STUDENT");
			userService.addRoleToUser("user@gmail.com","STUDENT");
			userService.addRoleToUser("admin@gmail.com","ADMIN");
			userService.addRoleToUser("manager@gmail.com","MANAGER");
			userService.addRoleToUser("teacher@gmail.com","TEACHER");
			//userService.addClick(new UserClicksOnCourseEntity());

			userService.saveUser(new UserEntity("teacher2@gmail.com","123","mine",false,true,false,false));
			userService.addRoleToUser("teacher2@gmail.com","TEACHER");
		};
	}*/



	/*@Bean
	CommandLineRunner run(ProgramEducationService programEducationService){
		return args -> {
			programEducationService.save(new EducationProgramEntity("Luyện thi PEN-C","LTDHPENC",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Luyện thi PEN-I","LTDHPENI",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Luyện thi DH Bách khoa","LTDHBK",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Lớp 12","CLASS12",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Lớp 11","ClASS11",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Lớp 10","CLASS10",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Tổng ôn","LTCL10TO",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Luyện đề","LTCL10LD",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Cấp tốc","LTCL10CT",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Lớp 9","CLASS9",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Lớp 8","CLASS8",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Lớp 7","CLASS7",new ArrayList<>()));
			programEducationService.save(new EducationProgramEntity("Lớp 6","CLASS6",new ArrayList<>()));
		};
	}*/


	/*@Bean
	CommandLineRunner run(ProgramEducationService programEducationService){
		return args -> {


			programEducationService.addSubjectToProgram("Luyện thi PEN-C","Toán");
			programEducationService.addSubjectToProgram("Luyện thi PEN-C","Ngữ văn");
			programEducationService.addSubjectToProgram("Luyện thi PEN-C","Tiếng Anh");
			programEducationService.addSubjectToProgram("Luyện thi PEN-C","Vật lí");
			programEducationService.addSubjectToProgram("Luyện thi PEN-C","Hóa học");
			programEducationService.addSubjectToProgram("Luyện thi PEN-I","Toán");
			programEducationService.addSubjectToProgram("Luyện thi PEN-I","Ngữ văn");
			programEducationService.addSubjectToProgram("Luyện thi PEN-I","Tiếng Anh");
			programEducationService.addSubjectToProgram("Luyện thi PEN-I","Vật lí");
			programEducationService.addSubjectToProgram("Lớp 12","Hóa học");
			programEducationService.addSubjectToProgram("Lớp 12","Toán");
			programEducationService.addSubjectToProgram("Lớp 12","Tiếng Anh");
			programEducationService.addSubjectToProgram("Lớp 12","Vật lí");
			programEducationService.addSubjectToProgram("Lớp 12","Ngữ văn");
			programEducationService.addSubjectToProgram("Lớp 11","Hóa học");
			programEducationService.addSubjectToProgram("Lớp 11","Toán");
			programEducationService.addSubjectToProgram("Lớp 11","Tiếng Anh");
			programEducationService.addSubjectToProgram("Lớp 11","Vật lí");
			programEducationService.addSubjectToProgram("Lớp 11","Ngữ văn");
			programEducationService.addSubjectToProgram("Lớp 10","Hóa học");
			programEducationService.addSubjectToProgram("Lớp 10","Toán");
			programEducationService.addSubjectToProgram("Lớp 10","Tiếng Anh");
			programEducationService.addSubjectToProgram("Lớp 10","Vật lí");
			programEducationService.addSubjectToProgram("Lớp 10","Ngữ văn");
			programEducationService.addSubjectToProgram("Tổng ôn","Ngữ văn");
			programEducationService.addSubjectToProgram("Tổng ôn","Toán");
			programEducationService.addSubjectToProgram("Tổng ôn","Tiếng Anh");
			programEducationService.addSubjectToProgram("Cấp tốc","Ngữ văn");
			programEducationService.addSubjectToProgram("Cấp tốc","Toán");
			programEducationService.addSubjectToProgram("Cấp tốc","Tiếng Anh");
			programEducationService.addSubjectToProgram("Lớp 9","Hóa học");
			programEducationService.addSubjectToProgram("Lớp 9","Toán");
			programEducationService.addSubjectToProgram("Lớp 9","Tiếng Anh");
			programEducationService.addSubjectToProgram("Lớp 9","Vật lí");
			programEducationService.addSubjectToProgram("Lớp 9","Ngữ văn");
			programEducationService.addSubjectToProgram("Lớp 8","Ngữ văn");
			programEducationService.addSubjectToProgram("Lớp 8","Toán");
			programEducationService.addSubjectToProgram("Lớp 8","Tiếng Anh");
			programEducationService.addSubjectToProgram("Lớp 7","Ngữ văn");
			programEducationService.addSubjectToProgram("Lớp 7","Toán");
			programEducationService.addSubjectToProgram("Lớp 7","Tiếng Anh");
			programEducationService.addSubjectToProgram("Lớp 6","Ngữ văn");
			programEducationService.addSubjectToProgram("Lớp 6","Toán");
			programEducationService.addSubjectToProgram("Lớp 6","Tiếng Anh");
			programEducationService.addSubjectToProgram("Luyện đề","Ngữ văn");
			programEducationService.addSubjectToProgram("Luyện đề","Toán");
			programEducationService.addSubjectToProgram("Luyện đề","Tiếng Anh");
		};
	}*/
}
