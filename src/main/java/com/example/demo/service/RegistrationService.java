package com.example.demo.service;

import com.example.demo.dto.RegistrationRequest;

public interface RegistrationService {
    String register(RegistrationRequest registrationRequest) throws IllegalAccessException;
    void confirmToken(String token);
}
