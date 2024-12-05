package com.msara.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "token"})
public record AuthLoginResponse(String username, String message, String authToken) {
}
