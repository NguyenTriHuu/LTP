package com.example.demo.api.web;

import com.example.demo.dto.*;
import com.example.demo.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class ChatApi {
    private final ChatBotService chatBotService;
    @PostMapping(value = "/chat/{idSession}")
    public ResponseEntity<String> chat (@RequestBody String message,@PathVariable("idSession") Long idChatSession){
        RestTemplate restTemplate =new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey ="Bearer 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";
        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization",apiKey);
        ChatGPTRequest chatGPTRequest =new ChatGPTRequest("gpt-3.5-turbo",message);
        HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(chatGPTRequest,headers);
        ChatGPTResponse chatGPTResponse =restTemplate.postForObject(url,entity, ChatGPTResponse.class);
        chatBotService.save(idChatSession,message,true);
        chatBotService.save(idChatSession,chatGPTResponse.getChoices().get(0).getMessage().getContent(),false);
        return ResponseEntity.ok().body(chatGPTResponse.getChoices().get(0).getMessage().getContent());
    }

    @PostMapping(value = "/chat/create-session")
    public ResponseEntity<Long> createSession (@RequestBody ChatSessionRequest chatSessionRequest){
        return ResponseEntity.ok().body(chatBotService.createSessionChat(chatSessionRequest.getUserName(), chatSessionRequest.getIdCourse()));
    }

    @GetMapping(value = "/chat/messages/{idSession}")
    public ResponseEntity<List<ChatBotResponse>> getMessageBySession(@PathVariable("idSession") Long idChatSession){
        return ResponseEntity.ok().body(chatBotService.getChatBySession(idChatSession));
    }
}
