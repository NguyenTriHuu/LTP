package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotResponse {
    private String message;
    private String sender;
    private LocalDateTime sentTime;
    private String direction;
}
