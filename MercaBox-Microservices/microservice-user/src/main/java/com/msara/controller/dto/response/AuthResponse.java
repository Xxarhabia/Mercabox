package com.msara.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message, auth_token, verify_token"})
public record AuthResponse(String username, String message, String authToken, String verifyToken){
}
