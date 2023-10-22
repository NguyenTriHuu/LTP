package com.example.demo.api;

import com.example.demo.dto.RegistrationRequest;
import com.example.demo.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/registration")
@RequiredArgsConstructor
@CrossOrigin

public class Registration {

    private final RegistrationService registrationService;

    @PostMapping
    public String Registration(@RequestBody RegistrationRequest registrationRequest) throws IllegalAccessException {
        return registrationService.register(registrationRequest);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
