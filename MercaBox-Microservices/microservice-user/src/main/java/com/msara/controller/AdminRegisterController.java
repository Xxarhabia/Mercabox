package com.msara.controller;

import com.msara.controller.dto.request.AuthRegisterRequest;
import com.msara.controller.dto.response.AuthResponse;
import com.msara.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminRegisterController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> adminRegister(@RequestBody AuthRegisterRequest authRegisterRequest) {
        try {
            return ResponseEntity.status(201).body(userDetailService.requestAdminRegister(authRegisterRequest));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body(new AuthResponse(null, ex.getMessage(), null, null));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        boolean isVerified = userDetailService.verifyEmail(token);
        if (isVerified) {
            return ResponseEntity.status(200).body("The user was successfully verified");
        } else {
            return ResponseEntity.status(500).body("Error verifying user");
        }
    }
}
