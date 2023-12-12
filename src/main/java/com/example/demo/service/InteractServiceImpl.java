package com.example.demo.service;

import com.example.demo.Entity.InteractEntity;
import com.example.demo.repo.BlogRepo;
import com.example.demo.repo.InteractRepo;
import com.example.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InteractServiceImpl implements InteractService{
    private final InteractRepo interactRepo;
    private final BlogRepo blogRepo;
    private final UserRepo userRepo;

    @Override
    public void save() {

    }

    @Override
    public void likeBlog(Long idBlog, Long idUser) {
        InteractEntity interactEntity = new InteractEntity();
        interactEntity.setBlog(blogRepo.findById(idBlog).get());
        interactEntity.setUser(userRepo.findById(idUser).get());
        interactRepo.save(interactEntity);
    }

    @Override
    public void dislikeBlog(Long idBlog, Long idUser) {
        interactRepo.dislikeBlog(idBlog,idUser);
    }
}
