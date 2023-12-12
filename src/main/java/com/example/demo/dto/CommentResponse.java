package com.example.demo.dto;

import com.example.demo.Entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long idComment;
    private String fullNameUser;
    private Date createdTime;
    private byte[] linkAvatarUser;
    private String content;
    private List<RepliesResponse> children;
    private String type;
    private String userName;
    private Long idUser;
}
