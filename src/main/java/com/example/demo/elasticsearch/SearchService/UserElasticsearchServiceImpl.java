package com.example.demo.elasticsearch.SearchService;

import com.example.demo.Entity.UserEntity;
import com.example.demo.elasticsearch.Repository.UserElasticsearchRepo;
import com.example.demo.elasticsearch.model.UserElasticsearchModel;
import com.example.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserElasticsearchServiceImpl implements UserElasticsearchService {
    private final UserElasticsearchRepo userElasticsearchRepo;
    private final UserRepo userRepo;
    @Override
    public List<UserElasticsearchModel> getAll() {
        Iterable<UserElasticsearchModel> itr =userElasticsearchRepo.findAll();
        List<UserElasticsearchModel> list = new ArrayList<>();
        if(itr!=null){
            itr.forEach(list::add);
        }
        return list;
    }

    @Override
    public List<UserEntity> findByFilter(String text) {
        if(text.equals("")){
            return userRepo.findAll();
        }
        List<UserElasticsearchModel> list =userElasticsearchRepo.search(text);
        List<UserEntity> users = new ArrayList<>();
        for(UserElasticsearchModel model: list){
            UserEntity userEntity = userRepo.findById(model.getId()).get();
            users.add(userEntity);
        }
        return users;
    }
}
