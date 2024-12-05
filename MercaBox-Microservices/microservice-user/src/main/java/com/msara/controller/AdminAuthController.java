package com.msara.controller;

import com.msara.controller.dto.request.AuthLoginRequest;
import com.msara.controller.dto.request.AuthRegisterRequest;
import com.msara.controller.dto.request.AuthVerifyUserRequest;
import com.msara.controller.dto.response.AuthLoginResponse;
import com.msara.controller.dto.response.AuthRegisterResponse;
import com.msara.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest authLoginRequest) {
        try {
            return ResponseEntity.status(200).body(userDetailService.loginRequest(authLoginRequest));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body(new AuthLoginResponse(null, ex.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponse> register(@RequestBody AuthRegisterRequest authRegisterRequest) {
        try {
            return ResponseEntity.status(201).body(userDetailService.requestRegister(authRegisterRequest));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body(new AuthRegisterResponse(null, ex.getMessage(), null, null));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody AuthVerifyUserRequest authVerifyUserRequest) {
        boolean isVerified = userDetailService.verifyEmail(authVerifyUserRequest);
        if (isVerified) {
            return ResponseEntity.status(200).body("The user was successfully verified");
        } else {
            return ResponseEntity.status(500).body("Error verifying user");
        }
    }
}
