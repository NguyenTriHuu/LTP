package com.example.demo.service;

import com.example.demo.Entity.*;
import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.DeleteCommentResponse;
import com.example.demo.dto.RepliesResponse;
import com.example.demo.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements  CommentService{
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final LectureRepo lectureRepo;
    private final UserService userService;
    private final RepliesRepo repliesRepo;
    private final BlogRepo blogRepo;
    @Override
    public CommentResponse addCommentToLesson(Long idLesson, CommentRequest commentRequest) {
        CommentEntity comment = new CommentEntity();
        UserEntity user = userRepo.findByUsername(commentRequest.getUserName()).get();
        comment.setUser(user);
        comment.setContent(commentRequest.getContent());
        LocalDateTime currentDateTime = LocalDateTime.now();
        comment.setCreatedtime(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        LectureEntity lesson = lectureRepo.findById(idLesson).get();
        comment.setConfigComment(lesson.getConfigComment());
        comment.setLecture(lesson);
        CommentEntity commentEntity= commentRepo.save(comment);

        //Set response
        CommentResponse commentResponse =new CommentResponse();
        commentResponse.setIdComment(commentEntity.getId());
        commentResponse.setCreatedTime(commentEntity.getCreatedtime());
        commentResponse.setFullNameUser(commentEntity.getUser().getFullname());
        commentResponse.setContent(commentEntity.getContent());
        commentResponse.setLinkAvatarUser(userService.downLoadAvatar(commentEntity.getUser().getId()));
        commentResponse.setType("COMMENT");
        commentResponse.setUserName(commentRequest.getUserName());
        commentResponse.setChildren(new ArrayList<RepliesResponse>());
        return commentResponse;
    }

    @Override
    public RepliesResponse addReplyToComment(Long idComment, CommentRequest commentRequest) {
        RepliesEntity repliesEntity =new RepliesEntity();
        repliesEntity.setContent(commentRequest.getContent());
        repliesEntity.setComment(commentRepo.findById(idComment).get());
        repliesEntity.setUser(userRepo.findByUsername(commentRequest.getUserName()).get());
        LocalDateTime currentDateTime = LocalDateTime.now();
        repliesEntity.setCreatedtime(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        RepliesEntity repliesEntity1 =repliesRepo.save(repliesEntity);

        //set response
        RepliesResponse response =new RepliesResponse();
        response.setContent(repliesEntity1.getContent());
        response.setIdComment(idComment);
        response.setReplyId(repliesEntity1.getId());
        response.setLinkAvatarUser(userService.downLoadAvatar(repliesEntity1.getUser().getId()));
        response.setFullNameUser(repliesEntity1.getUser().getFullname());
        response.setCreatedTime(repliesEntity1.getCreatedtime());
        response.setType("REPLY");
        response.setUserName(commentRequest.getUserName());
        response.setIdUser(repliesEntity1.getUser().getId());
        return response;
    }


    private void extracted(Long idLesson, List<CommentResponse> list) {
        for(CommentEntity commentEntity: commentRepo.getAll(idLesson)){
            CommentResponse commentResponse =new CommentResponse();
            commentResponse.setIdComment(commentEntity.getId());
            commentResponse.setCreatedTime(commentEntity.getCreatedtime());
            commentResponse.setFullNameUser(commentEntity.getUser().getFullname());
            commentResponse.setContent(commentEntity.getContent());
            commentResponse.setLinkAvatarUser(userService.downLoadAvatar(commentEntity.getUser().getId()));
            commentResponse.setType("COMMENT");
            commentResponse.setUserName(commentEntity.getUser().getUsername());
            List<RepliesEntity> repliesEntityList =repliesRepo.findByComment_Id(commentEntity.getId());
            if(repliesEntityList!= null){
                List <RepliesResponse> listReplies =new ArrayList<>();
                for(RepliesEntity repliesEntity: repliesEntityList){
                    RepliesResponse response =new RepliesResponse();
                    response.setContent(repliesEntity.getContent());
                    response.setIdComment(commentEntity.getId());
                    response.setReplyId(repliesEntity.getId());
                    response.setLinkAvatarUser(userService.downLoadAvatar(repliesEntity.getUser().getId()));
                    response.setFullNameUser(repliesEntity.getUser().getFullname());
                    response.setCreatedTime(repliesEntity.getCreatedtime());
                    response.setType("REPLY");
                    response.setUserName(repliesEntity.getUser().getUsername());
                   listReplies.add(response);
                }
                commentResponse.setChildren(listReplies);
            }
            list.add(commentResponse);
        }
    }

    @Override
    public List<CommentResponse> getAllComments(Long idLesson) {
        List<CommentResponse> list =new ArrayList<>();
        extracted(idLesson, list);
        return list;
    }

    @Override
    public List<CommentResponse> getAllCommentsBlog(Long idBlog) {
        List<CommentResponse> list =new ArrayList<>();
        for(CommentEntity commentEntity: commentRepo.getAllByBlog(idBlog)){
            CommentResponse commentResponse =new CommentResponse();
            commentResponse.setIdComment(commentEntity.getId());
            commentResponse.setCreatedTime(commentEntity.getCreatedtime());
            commentResponse.setFullNameUser(commentEntity.getUser().getFullname());
            commentResponse.setContent(commentEntity.getContent());
            commentResponse.setType("COMMENT");
            commentResponse.setUserName(commentEntity.getUser().getUsername());
            commentResponse.setIdUser(commentEntity.getUser().getId());
            List<RepliesEntity> repliesEntityList =repliesRepo.findByComment_Id(commentEntity.getId());
            if(repliesEntityList!= null){
                List <RepliesResponse> listReplies =new ArrayList<>();
                for(RepliesEntity repliesEntity: repliesEntityList){
                    RepliesResponse response =new RepliesResponse();
                    response.setContent(repliesEntity.getContent());
                    response.setIdComment(commentEntity.getId());
                    response.setReplyId(repliesEntity.getId());
                    response.setFullNameUser(repliesEntity.getUser().getFullname());
                    response.setCreatedTime(repliesEntity.getCreatedtime());
                    response.setType("REPLY");
                    response.setUserName(repliesEntity.getUser().getUsername());
                    response.setIdUser(repliesEntity.getUser().getId());
                    listReplies.add(response);
                }
                commentResponse.setChildren(listReplies);
            }
            list.add(commentResponse);
        }
        return list;
    }

    @Override
    public DeleteCommentResponse deleteComment(Long idComment) {
        repliesRepo.deleteByComment(idComment);
        commentRepo.deleteByComment(idComment);
        DeleteCommentResponse response = new DeleteCommentResponse();
        response.setCommentId(idComment);
        return response;
    }

    @Override
    public DeleteCommentResponse deleteCommentReply(Long idReply) {
        repliesRepo.deleteByReply(idReply);
        DeleteCommentResponse response = new DeleteCommentResponse();
        response.setReplyId(idReply);
        return response;
    }

    @Override
    public CommentResponse addCommentToBlog(Long blogId, CommentRequest commentRequest) {
        CommentEntity comment = new CommentEntity();
        UserEntity user = userRepo.findByUsername(commentRequest.getUserName()).get();
        comment.setUser(user);
        comment.setContent(commentRequest.getContent());
        LocalDateTime currentDateTime = LocalDateTime.now();
        comment.setCreatedtime(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        BlogEntity blogEntity = blogRepo.findById(blogId).get();
        comment.setBlog(blogEntity);
        CommentEntity commentEntity= commentRepo.save(comment);

        //Set response
        CommentResponse commentResponse =new CommentResponse();
        commentResponse.setIdComment(commentEntity.getId());
        commentResponse.setCreatedTime(commentEntity.getCreatedtime());
        commentResponse.setFullNameUser(commentEntity.getUser().getFullname());
        commentResponse.setContent(commentEntity.getContent());
        commentResponse.setType("COMMENT");
        commentResponse.setUserName(commentRequest.getUserName());
        commentResponse.setChildren(new ArrayList<RepliesResponse>());
        commentResponse.setIdUser(commentEntity.getUser().getId());
        return commentResponse;
    }

}
