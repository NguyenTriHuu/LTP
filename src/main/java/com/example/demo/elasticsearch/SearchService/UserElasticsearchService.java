package com.example.demo.elasticsearch.SearchService;

import com.example.demo.Entity.UserEntity;
import com.example.demo.elasticsearch.model.UserElasticsearchModel;

import java.util.List;

public interface UserElasticsearchService {
    List<UserElasticsearchModel> getAll();
    List<UserEntity> findByFilter(String text);
}
