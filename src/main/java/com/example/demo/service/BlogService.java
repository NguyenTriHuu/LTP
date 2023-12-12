package com.example.demo.service;

import com.example.demo.dto.BlogRequest;
import com.example.demo.dto.BlogResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogService {
    void save(BlogRequest blogRequest);
    Page<BlogResponse> getAll(int page,int size);

    Page<BlogResponse> getAllByCategory(int page,int size,Long idCategory);

    byte[] loadThumnail(Long idBlog);

    BlogResponse getBlogById(Long id);

    Page<BlogResponse> search(int page,int size, String search);

    List<BlogResponse> getAllBlogByUser(String userName);

    void block(Long idBlog, Boolean block);

    List<BlogResponse> searchMyBlog(String search,String userName);

    BlogResponse getById(Long id);

    void update(BlogRequest blogRequest , Long idBlog);
}
