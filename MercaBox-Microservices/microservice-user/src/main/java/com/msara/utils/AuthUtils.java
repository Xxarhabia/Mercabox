package com.msara.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AuthUtils {

    public int generateVerificationCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
