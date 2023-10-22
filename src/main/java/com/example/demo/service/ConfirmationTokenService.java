package com.example.demo.service;

import com.example.demo.Entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken confirmationToken);
    Optional<ConfirmationToken> getToken(String token);

    void setConfirmedAt(String token);
}
