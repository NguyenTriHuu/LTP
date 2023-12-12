package com.example.demo.repo;

import com.example.demo.Entity.InteractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InteractRepo extends JpaRepository<InteractEntity,Long> {
    @Query(value = " select count(*) from interact where blog_id =?1", nativeQuery = true)
    int countInteractionBlog(Long id);

    @Query(value = "select count(*) from interact where user_id =?1 and blog_id=?2",nativeQuery = true)
    int likedBlog(Long idUser, Long idBlog);

    @Query(value = "select * from interact where blog_id=?1 and user_id=?2" , nativeQuery = true)
    InteractEntity getByBlogAndUsser (Long idBlog,Long idUser);

    @Modifying
    @Query(value = "DELETE FROM interact WHERE blog_id=?1 and user_id=?2",nativeQuery = true)
    void dislikeBlog(Long idBlog,Long idUser);
}
