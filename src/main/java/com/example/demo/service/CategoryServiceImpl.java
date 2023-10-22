package com.example.demo.service;

import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.EducationProgramEntity;
import com.example.demo.Entity.SubjectEntity;
import com.example.demo.dto.CategoryResponse;
import com.example.demo.dto.CategoryforSubjectRes;
import com.example.demo.dto.ProgramEducationResponse;
import com.example.demo.dto.SubjectReponse;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ProgramEducationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepo categoryRepo;
    private final ProgramEducationRepo programEducationRepo;
    @Override
    public List<CategoryResponse> finAllForTree() {
        List<CategoryResponse> list =new ArrayList<>();
        List<CategoryEntity> listCategory=categoryRepo.findAll();
        for(CategoryEntity category:listCategory){
            CategoryResponse categoryResponse =new CategoryResponse();
            categoryResponse.setNameCategoryResponse(category.getName());
            categoryResponse.setCodeCategory(category.getCode());
            List<ProgramEducationResponse> listProgram =new ArrayList<>();
            for(EducationProgramEntity educationProgram: category.getPrograms()){
                ProgramEducationResponse programEducationResponse= new ProgramEducationResponse();
                programEducationResponse.setNameProgramEducationResponse(educationProgram.getName());
                programEducationResponse.setCodeProgram(educationProgram.getCode());
                List<SubjectReponse> subjectResponse = new ArrayList<>();
                for(SubjectEntity subject :educationProgram.getSubjects()){
                    SubjectReponse sub =new SubjectReponse();
                    sub.setCode(subject.getCode());
                    sub.setName(subject.getName());
                    subjectResponse.add(sub);
                }
                programEducationResponse.setSubjectResponseList(subjectResponse);
                listProgram.add(programEducationResponse);
            }
            categoryResponse.setEducationResponseList(listProgram);
            list.add(categoryResponse);
        }

        return list ;
    }

    @Override
    public void save(CategoryEntity categoryEntity) {
        categoryRepo.save(categoryEntity);
    }

    @Override
    public void addProgramToCategory(long idCategory, long idProgram) {
        CategoryEntity category= categoryRepo.findById(idCategory).get();
        EducationProgramEntity program = programEducationRepo.findById(idProgram).get();
        category.getPrograms().add(program);
    }

    @Override
    public CategoryforSubjectRes findCategoryBySubject(Long idSub) {
        return categoryRepo.findCategoryBySubject(idSub);
    }
}
