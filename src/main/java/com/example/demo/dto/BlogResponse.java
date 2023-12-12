package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {
    private Long id;
    private Long idUser;
    private String nameUser;
    private String shortDescription;
    private String content;
    private String title;
    private LocalDateTime dateCreate;
    private Boolean liked;
    private int numOfComment;
    private Boolean status;
    private int numOfInteract;
    private LocalDateTime dateModified;
    private int views;
    private Long idCategory;
    public BlogResponse(Long id, Long idUser, String shortDescription, String title, LocalDateTime dateCreate,Boolean status) {
        this.id = id;
        this.idUser = idUser;
        this.shortDescription = shortDescription;
        this.title = title;
        this.dateCreate = dateCreate;
        this.numOfComment = numOfComment;
        this.numOfInteract = numOfInteract;
        this.status = status;
    }



    public BlogResponse(Long id, Long idUser, String nameUser, String shortDescription, String title, LocalDateTime dateCreate) {
        this.id = id;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.shortDescription = shortDescription;
        this.title = title;
        this.dateCreate = dateCreate;
    }

    public BlogResponse(Long id, Long idUser, String content, String title, Long idCategory) {
        this.id = id;
        this.idUser = idUser;
        this.content = content;
        this.title = title;
        this.idCategory = idCategory;
    }
}
