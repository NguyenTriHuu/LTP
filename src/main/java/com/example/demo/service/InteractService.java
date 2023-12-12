package com.example.demo.service;

public interface InteractService {
    void save ();

    void likeBlog(Long idBlog, Long idUser);

    void dislikeBlog(Long idBlog, Long idUser);
}
