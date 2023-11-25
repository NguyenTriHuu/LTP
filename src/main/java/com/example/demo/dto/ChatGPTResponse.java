package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPTResponse {
    private List<Choice> choices = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class Choice{
        private int index;
        private MessageChat message;
    }
}
