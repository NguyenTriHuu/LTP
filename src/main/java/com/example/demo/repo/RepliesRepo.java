package com.example.demo.repo;

import com.example.demo.Entity.RepliesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepliesRepo extends JpaRepository<RepliesEntity ,Long> {
    List<RepliesEntity> findByComment_Id(Long commentId);

    @Modifying
    @Query(value = "DELETE FROM replies WHERE comment_id =:idComment", nativeQuery = true)
    void deleteByComment(@Param("idComment") Long idComment);

    @Modifying
    @Query(value = "DELETE FROM replies WHERE id =:idReply", nativeQuery = true)
    void deleteByReply(@Param("idReply") Long idReply);
}
