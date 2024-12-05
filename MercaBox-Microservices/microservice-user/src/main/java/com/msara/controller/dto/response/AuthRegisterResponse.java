package com.msara.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message, auth_token, verification_code"})
public record AuthRegisterResponse(String username, String message, String authToken, String verificationCode){
}
