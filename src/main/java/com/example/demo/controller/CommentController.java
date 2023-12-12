package com.example.demo.controller;

import com.amazonaws.services.frauddetector.model.Variable;
import com.example.demo.Entity.CommentEntity;
import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.DeleteCommentResponse;
import com.example.demo.dto.RepliesResponse;
import com.example.demo.repo.CommentRepo;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {
    private final CommentService commentService;

    @MessageMapping("/comment/{lessonId}")
    @SendTo("/topic/comments/{lessonId}")
    public CommentResponse sendComment(@DestinationVariable Long lessonId , CommentRequest commentRequest) {
        return commentService.addCommentToLesson(lessonId,commentRequest);
    }

    @MessageMapping("/comment/reply/{commentId}/{lessonId}")
    @SendTo("/topic/comments/{lessonId}")
    public RepliesResponse sendReply(@DestinationVariable Long commentId , @DestinationVariable Long lessonId, CommentRequest commentRequest) {
        return commentService.addReplyToComment(commentId,commentRequest);
    }

    @MessageMapping("/comment/{commentId}/{lessonId}/delete")
    @SendTo("/topic/comments/{lessonId}")
    public DeleteCommentResponse deleteCommment(@DestinationVariable Long commentId , @DestinationVariable Long lessonId) {
        return commentService.deleteComment(commentId);
    }

    @MessageMapping("/comment/reply/{idReply}/{lessonId}/delete")
    @SendTo("/topic/comments/{lessonId}")
    public DeleteCommentResponse deleteCommmentReply(@DestinationVariable Long idReply , @DestinationVariable Long lessonId) {
        return commentService.deleteCommentReply(idReply);
    }


}
