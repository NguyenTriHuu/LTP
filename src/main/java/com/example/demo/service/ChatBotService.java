package com.example.demo.service;

import com.example.demo.dto.ChatBotResponse;

import java.util.List;

public interface ChatBotService {
    void save (Long idChatBotSession,String message, Boolean isUserMessage);

    Long createSessionChat (String userName, Long idCourse);

    List<ChatBotResponse> getChatBySession(Long idSession );
}
