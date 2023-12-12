package com.example.demo.repo;

import com.example.demo.Entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepo extends JpaRepository<CommentEntity , Long> {

    @Query(value = "select * from comment where lecture_id =:idLesson ORDER BY createdtime DESC",nativeQuery = true)
    List<CommentEntity> getAll(@Param("idLesson") Long idLesson);

    @Modifying
    @Query(value = "DELETE FROM comment WHERE id =:idComment", nativeQuery = true)
    void deleteByComment(@Param("idComment") Long idComment);

    @Query(value = "select count(*) from comment where blog_id =?1",nativeQuery = true)
    Integer countComment(Long idBlog);

    @Query(value = "select * from comment where blog_id =:idBlog ORDER BY createdtime DESC",nativeQuery = true)
    List<CommentEntity> getAllByBlog(@Param("idBlog") Long idBlog);
}
