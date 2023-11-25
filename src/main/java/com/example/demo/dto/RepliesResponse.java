package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepliesResponse {
    private Long replyId;
    private String fullNameUser;
    private Date createdTime;
    private byte[] linkAvatarUser;
    private String content;
    private Long idComment;
    private String type;
    private String userName;
}
