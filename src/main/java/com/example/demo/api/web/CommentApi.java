package com.example.demo.api.web;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.RepliesResponse;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class CommentApi {
    private final CommentService commentService;
    @GetMapping("/course/lecture/{lessonId}/comment")
    public ResponseEntity<List<CommentResponse>> getAllComment (@PathVariable("lessonId") Long id){
        return ResponseEntity.ok().body(commentService.getAllComments(id));
    }

    @GetMapping("/blog/{idBlog}/comment")
    public ResponseEntity<List<CommentResponse>> getAllCommentBlog (@PathVariable("idBlog") Long id){
        return ResponseEntity.ok().body(commentService.getAllCommentsBlog(id));
    }

 /*  @PostMapping("/course/test/comment/{id}")
    public List<CommentResponse> sendComment(@PathVariable("id") Long id, @RequestBody CommentRequest commentRequest) {
        return commentService.addCommentToLesson(id,commentRequest);
    }

    @GetMapping("/course/lecture/comment/child")
    public ResponseEntity<List<CommentResponse>> getChildrenComments (@RequestParam String ids){
        Long[] idArray = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toArray(Long[]::new);
        return ResponseEntity.ok().body(commentService.getChildrenComments(idArray));
    }*/

    @PostMapping("/course/test/comment/{id}")
    public CommentResponse sendComment(@PathVariable("id") Long id, @RequestBody CommentRequest commentRequest) {
        return commentService.addCommentToLesson(id,commentRequest);
    }

    @PostMapping("/course/test/comment/reply/{id}")
    public RepliesResponse senR(@PathVariable("id") Long id, @RequestBody CommentRequest commentRequest) {
        return commentService.addReplyToComment(id,commentRequest);
    }

}
