package com.example.demo.api;

import com.example.demo.dto.RegistrationRequest;
import com.example.demo.service.RegistrationService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "api/registration")
@RequiredArgsConstructor
@CrossOrigin

public class Registration {

    private final RegistrationService registrationService;
    private final static Logger LOG = LoggerFactory.getLogger(Registration.class);
    private final UserService userService;
    private Map<String, DeferredResult<Boolean>> deferredResults = new HashMap<>();
    @PostMapping
    public String Registration(@RequestBody RegistrationRequest registrationRequest) throws IllegalAccessException {
        return registrationService.register(registrationRequest);
    }

  /*  @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }*/

    @GetMapping("/async-deferredresult")
    public DeferredResult<Boolean> handleReq(@RequestParam("userName") String userName) {
        DeferredResult<Boolean> deferredResult = new DeferredResult<>(600000L);
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body("Request timeout occurred."));
        });
        deferredResult.onCompletion(() -> {
            deferredResults.remove(userName);
        });
        deferredResults.put(userName, deferredResult);
        return deferredResult;
    }

    @GetMapping(path = "confirm")
    public void onEmailActivated(@RequestParam("userName") String userName,@RequestParam("token") String token) {
        userService.setVerifiUser(userName);
        registrationService.confirmToken(token);
        CompletableFuture.runAsync(() -> {
            LOG.info("Processing in separate thread");
            try {
                DeferredResult<Boolean> deferredResult = deferredResults.remove(userName);
                if (deferredResult != null) {
                    deferredResult.setResult(true);
                }
            } catch (Exception e) {
                LOG.info("Request processing interrupted");
                DeferredResult<Boolean> deferredResult = deferredResults.remove(userName);
                if (deferredResult != null) {
                    deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("INTERNAL_SERVER_ERROR occurred."));
                }

        }});
    }

}
