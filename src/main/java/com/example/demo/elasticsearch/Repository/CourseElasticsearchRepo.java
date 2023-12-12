package com.example.demo.elasticsearch.Repository;

import com.example.demo.elasticsearch.model.CourseElasticsearchModel;
import com.example.demo.elasticsearch.model.UserElasticsearchModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CourseElasticsearchRepo extends ElasticsearchRepository<CourseElasticsearchModel,Long> {
    @Query("{\"bool\": {\"should\": [" +
            "{\"match\": {\"subject_name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"title\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"short_description\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"program_code\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"categogy_name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"description\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"program_name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            "{\"match\": {\"category_code\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}" +
            "{\"match\": {\"subject_code\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}" +
            "]}}")
    List<CourseElasticsearchModel> search(String text);
}
