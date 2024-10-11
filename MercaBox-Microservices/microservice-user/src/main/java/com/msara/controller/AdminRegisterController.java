package com.msara.controller;

import com.msara.controller.dto.request.AuthRegisterRequest;
import com.msara.controller.dto.response.AuthResponse;
import com.msara.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminRegisterController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    public void adminRegister(@RequestBody AuthRegisterRequest authRegisterRequest) {

    }
}
