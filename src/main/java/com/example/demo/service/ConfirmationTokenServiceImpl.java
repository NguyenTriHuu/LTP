package com.example.demo.service;

import com.example.demo.Entity.ConfirmationToken;
import com.example.demo.repo.ConfirmationTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl  implements ConfirmationTokenService{

    private final ConfirmationTokenRepo confirmationTokenRepo;
    @Override
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
            confirmationTokenRepo.save(confirmationToken);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return Optional.of(confirmationTokenRepo.findByToken(token).get());
    }

    @Override
    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken= confirmationTokenRepo.findByToken(token).get();
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepo.save(confirmationToken);
    }

}
