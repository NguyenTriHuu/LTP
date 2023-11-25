package com.example.demo.service;

import com.example.demo.Entity.ChatMessageEntity;
import com.example.demo.Entity.ChatSessionEntity;
import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.ChatBotResponse;
import com.example.demo.repo.ChatMessageRepo;
import com.example.demo.repo.ChatSessionRepo;
import com.example.demo.repo.CourseRepo;
import com.example.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatBotServiceImpl implements ChatBotService{
    private final ChatSessionRepo chatSessionRepo;
    private final ChatMessageRepo chatMessageRepo;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    @Override
    public void save(Long idChatBotSession,String message, Boolean isUserMessage) {
        ChatSessionEntity chatSessionEntity = chatSessionRepo.findById(idChatBotSession).get();
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setContent(message);
        chatMessageEntity.setIsUserMessage(isUserMessage);
        chatMessageEntity.setTimeStamp(LocalDateTime.now());
        chatSessionEntity.getMessages().add(chatMessageRepo.save(chatMessageEntity));
    }

    @Override
    public Long createSessionChat(String userName, Long idCourse) {
        ChatSessionEntity chatSessionEntity = new ChatSessionEntity();
        UserEntity user = userRepo.findByUsername(userName).get();
        CourseEntity course = courseRepo.findById(idCourse).get();
        chatSessionEntity.setUser(user);
        chatSessionEntity.setCourse(course);
        chatSessionEntity.setStartTime(LocalDateTime.now());
        chatSessionEntity.setMessages(new ArrayList<>());
        return chatSessionRepo.save(chatSessionEntity).getId();
    }

    @Override
    public List<ChatBotResponse> getChatBySession(Long idSession) {
        List<ChatBotResponse> list = new ArrayList<>();
        ChatSessionEntity chatSession = chatSessionRepo.findById(idSession).get();
        for(ChatMessageEntity chatMessage: chatSession.getMessages()){
            ChatBotResponse chatBotResponse =new ChatBotResponse();
            if(chatMessage.getIsUserMessage()){
                chatBotResponse.setSender("USER");
                chatBotResponse.setDirection("outgoing");
            }else{
                chatBotResponse.setSender("BOT");
            }

            chatBotResponse.setSentTime(chatMessage.getTimeStamp());
            chatBotResponse.setMessage(chatMessage.getContent());
            list.add(chatBotResponse);
        }
        return list;
    }
}
