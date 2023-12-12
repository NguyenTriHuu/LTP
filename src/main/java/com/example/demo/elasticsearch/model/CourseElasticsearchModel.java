package com.example.demo.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(indexName = "course")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseElasticsearchModel {
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String subject_name;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String link_video_intro;
    private LocalDateTime date_start;
    @Field(type = FieldType.Text)
    private String short_description;
    @Field(type = FieldType.Text)
    private String program_code;
    @Field(type = FieldType.Text)
    private String categogy_name;
    @Field(type = FieldType.Text)
    private String description;
    private LocalDateTime modifiedtime;
    private int price;
    private int duration;
    private Boolean status;
    @Field(type = FieldType.Text)
    private String program_name;
    @Field(type = FieldType.Text)
    private String category_code;
    @Field(type = FieldType.Text)
    private String subject_code;
    @Field(type = FieldType.Text)
    private String image;

}
