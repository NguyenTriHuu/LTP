package com.example.demo.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(indexName = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserElasticsearchModel {
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String addressname;
    private LocalDateTime active;
    private LocalDateTime date_of_birth;
    @Field(type = FieldType.Text)
    private String fullname;
    @Field(type = FieldType.Text)
    private String street;
    private LocalDateTime createtime;
    private LocalDateTime modifiedtime;
    private Boolean locked;
    @Field(type = FieldType.Text)
    private String postal_code;
    @Field(type = FieldType.Text)
    private String rolename;
    @Field(type = FieldType.Text)
    private String username;
    @Field(type = FieldType.Text)
    private String country;
    @Field(type = FieldType.Text)
    private String city;

}
