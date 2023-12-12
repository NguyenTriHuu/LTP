package com.example.demo.repo;

import com.example.demo.Entity.BlogEntity;
import com.example.demo.dto.BlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepo extends JpaRepository<BlogEntity, Long> {

    @Query("select new com.example.demo.dto.BlogResponse(b.id,u.id,u.username,b.shortDescription,b.title,b.dateCreate) from BlogEntity b join UserEntity u on b.user.id = u.id where b.status=true")
    Page<BlogResponse>  getAllBlog (Pageable pageable);

    @Query("select new com.example.demo.dto.BlogResponse(b.id,u.id,u.username,b.shortDescription,b.title,b.dateCreate) from BlogEntity b join UserEntity u on b.user.id = u.id join CategoryEntity c on b.category.id = c.id  where c.id =?1 and b.status=true")
    Page<BlogResponse>  getAllByCategory (Pageable pageable,Long id);

    @Query("select new com.example.demo.dto.BlogResponse(b.id,u.id,u.username,b.shortDescription,b.title,b.dateCreate) from BlogEntity b join UserEntity u on b.user.id = u.id where b.status=true and lower(b.title) like lower(?1)")
    Page<BlogResponse>  search (Pageable pageable, String search);

    @Query("select new com.example.demo.dto.BlogResponse(b.id,u.id,b.shortDescription,b.title,b.dateCreate,b.status) from BlogEntity b join UserEntity u on b.user.id = u.id where u.id=?1")
    List<BlogResponse> getBlogByUser (Long search);

    @Query("select new com.example.demo.dto.BlogResponse(b.id,u.id,b.shortDescription,b.title,b.dateCreate,b.status) from BlogEntity b join UserEntity u on b.user.id = u.id where u.id=?1 and lower(b.title) like lower(?2) order by b.dateCreate DESC ")
    List<BlogResponse>  searchMyBlog ( Long idUser ,String search);

    @Query("select new com.example.demo.dto.BlogResponse(b.id,u.id,b.content,b.title,b.category.id) from BlogEntity b join UserEntity u on b.user.id = u.id where b.id =?1 ")
    BlogResponse getByIdBlog (Long id);
}
