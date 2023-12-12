package com.example.demo.elasticsearch.Repository;

import com.example.demo.elasticsearch.model.UserElasticsearchModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.List;

public interface UserElasticsearchRepo extends ElasticsearchRepository<UserElasticsearchModel,Long> {

    @Query("{\"bool\": {\"should\": [" +
            "{\"match\": {\"fullname\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"address_name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"street\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"postal_code\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"rolename\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"username\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"country\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"city\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}" +
            "]}}")
    List<UserElasticsearchModel> search(String text);
}

