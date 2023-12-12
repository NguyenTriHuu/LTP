package com.example.demo.controller;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.DeleteCommentResponse;
import com.example.demo.dto.RepliesResponse;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class CommentBlogController {
    private final CommentService commentService;

    @MessageMapping("/comment/blog/{blogId}")
    @SendTo("/topic/comments/blog/{blogId}")
    public CommentResponse sendComment(@DestinationVariable Long blogId , CommentRequest commentRequest) {
        return commentService.addCommentToBlog(blogId,commentRequest);
    }

    @MessageMapping("/comment/reply/blog/{commentId}/{blogId}")
    @SendTo("/topic/comments/blog/{blogId}")
    public RepliesResponse sendReply(@DestinationVariable Long commentId , @DestinationVariable Long blogId, CommentRequest commentRequest) {
        return commentService.addReplyToComment(commentId,commentRequest);
    }

    @MessageMapping("/comment/blog/{commentId}/{blogId}/delete")
    @SendTo("/topic/comments/blog/{blogId}")
    public DeleteCommentResponse deleteCommment(@DestinationVariable Long commentId , @DestinationVariable Long blogId) {
        return commentService.deleteComment(commentId);
    }

    @MessageMapping("/comment/reply/blog/{idReply}/{blogId}/delete")
    @SendTo("/topic/comments/blog/{blogId}")
    public DeleteCommentResponse deleteCommmentReply(@DestinationVariable Long idReply , @DestinationVariable Long blogId) {
        return commentService.deleteCommentReply(idReply);
    }
}
