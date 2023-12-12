package com.example.demo.service;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.DeleteCommentResponse;
import com.example.demo.dto.RepliesResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addCommentToLesson(Long idLesson, CommentRequest commentRequest);

    RepliesResponse addReplyToComment(Long idComment, CommentRequest commentRequest);

    List<CommentResponse> getAllComments (Long idLesson);
    List<CommentResponse> getAllCommentsBlog (Long idLesson);

    DeleteCommentResponse deleteComment (Long idComment);
    DeleteCommentResponse deleteCommentReply (Long idReply);

    CommentResponse addCommentToBlog(Long blogId,CommentRequest commentRequest);
}
