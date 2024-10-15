package com.msara.utils;

import org.apache.commons.lang.RandomStringUtils;

public class VerificationCodeGenerator {

    public static String generateVerificationCode() {
        return RandomStringUtils.randomAlphanumeric(6);
    }
}
